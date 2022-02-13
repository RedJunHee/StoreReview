package com.review.storereview.controller.cms;

import com.amazonaws.util.CollectionUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.ParamValidationException;
import com.review.storereview.common.exception.PersonIdNotFoundException;
import com.review.storereview.common.utils.CryptUtils;
import com.review.storereview.common.utils.StringUtil;
import com.review.storereview.dao.JWTUserDetails;
import com.review.storereview.dao.cms.Review;
import com.review.storereview.dao.cms.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.ReviewUpdateRequestDto;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import com.review.storereview.dto.response.ReviewFindResponseDto;
import com.review.storereview.dto.response.ReviewFindListResponseDto;
import com.review.storereview.dto.response.ReviewResponseDto;
import com.review.storereview.service.S3Service;
import com.review.storereview.service.cms.CommentService;
import com.review.storereview.service.cms.ReviewServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * {@Summary 리뷰 api 요청 컨트롤러 }
 * Author      : 문 윤 지
 * History     : [2022-01-23]
 */

@RestController
public class ReviewApiController {
    private final Logger logger = LoggerFactory.getLogger(ReviewApiController.class);

    private final ReviewServiceImpl reviewService;
    private final CommentService commentService;
    private final CryptUtils cryptUtils;
    private final S3Service s3Service;

    @Autowired
    public ReviewApiController(ReviewServiceImpl reviewService, CommentService commentService, CryptUtils cryptUtils, S3Service s3Service) {
        this.reviewService = reviewService;
        this.commentService = commentService;
        this.cryptUtils = cryptUtils;
        this.s3Service = s3Service;
    }

    /**
     * {@Summary 특정 가게에 대한 전체 리뷰 조회 컨트롤러}
     * @param placeId
     */
    @GetMapping("/places/{placeId}")
    public ResponseEntity<ResponseJsonObject> findAllReviews(@PathVariable String placeId) {
        // 1. findAll 서비스 로직
        List<Review> findReviews = reviewService.listAllReviews(placeId);// 해당하는 장소 관련 리뷰들 모두 조회하여 리스트

        // 2.1. placeAvgStars 계산
        Double placeAvgStars = reviewService.AveragePlaceStars(findReviews);

        // 3. listResponseDto 생성 및 추가
        ReviewFindListResponseDto listResponseDto = new ReviewFindListResponseDto(placeAvgStars);
        for (Review review : findReviews) {
            // 3.1. 관련 코멘트 갯수
            int commentNum = commentService.findCommentNumByReviewId(review.getReviewId());
            // 3.1. content, ImgUrl 인코딩
            String encodedContent = CryptUtils.Base64Encoding(review.getContent());
            List<String> encodedImgUrlList = null;
            if (!Objects.isNull(review.getImgUrl())) {
                if (!review.getImgUrl().isEmpty())
                     encodedImgUrlList = new ArrayList<>();
                    for (String imgUrl : review.getImgUrl()) {
                        encodedImgUrlList.add(CryptUtils.Base64Encoding(imgUrl));
                    }
            }
            // 3.2. responseDto 추가
            try {
                listResponseDto.addReview(
                        new ReviewFindResponseDto(
                                review.getReviewId(),
                                cryptUtils.AES_Encode(review.getUser().getSaid()),
                                review.getUser().getUserId(),
                                review.getStars(),
                                encodedContent,
                                encodedImgUrlList,
                                StringUtil.DateTimeToString(review.getCreatedAt()),
                                StringUtil.DateTimeToString(review.getUpdatedAt()),
                                review.getIsDelete(),
                                commentNum
                        )
                );
            } catch (Exception ex) {
                logger.error("ReviewApiController.findAllReviews Method/ Said Encoding Exception : " + ex.getMessage());
                ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR.getCode(), ApiStatusCode.SYSTEM_ERROR.getType(), ApiStatusCode.SYSTEM_ERROR.getMessage());
                return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()).setData(listResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 한개의 리뷰 조회 컨트롤러 }
     * @param reviewId
     */
    @GetMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseJsonObject> findOneReview(@PathVariable Long reviewId) {
        // 1. 조회 서비스 로직 (리뷰 조회 - userId 조회)
        Review findReview = reviewService.listReview(reviewId);
        if (findReview == null) {
            return new ResponseEntity<>(new PersonIdNotFoundException().getResponseJsonObject(),
                    HttpStatus.BAD_REQUEST);
        }
        // 2. content, ImgUrl 인코딩
        String encodedContent = CryptUtils.Base64Encoding(findReview.getContent());
        List<String> encodedImgUrlList = null;

        if (!Objects.isNull(findReview.getImgUrl())) {
            if (!findReview.getImgUrl().isEmpty())
                 encodedImgUrlList = new ArrayList<>();
                for (String imgUrl : findReview.getImgUrl()) {
                    encodedImgUrlList.add(CryptUtils.Base64Encoding(imgUrl));
                }
        }
        // 3. 관련 코멘트 갯수
        int commentNum = commentService.findCommentNumByReviewId(findReview.getReviewId());
        // 4. responseDto 생성
        ReviewFindResponseDto reviewResponseDto = null;
        try {
            reviewResponseDto = new ReviewFindResponseDto(
                    findReview.getReviewId(), cryptUtils.AES_Encode(findReview.getUser().getSaid()), findReview.getUser().getUserId(),
                    findReview.getStars(), encodedContent,
                    encodedImgUrlList,
                    StringUtil.DateTimeToString(findReview.getCreatedAt()),
                    StringUtil.DateTimeToString(findReview.getUpdatedAt()),
                    findReview.getIsDelete(), commentNum);
        } catch (Exception ex) {
            logger.error("ReviewApiController.findOneReview Method/ Said Encoding Exception : " + ex.getMessage());
            ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR.getCode(), ApiStatusCode.SYSTEM_ERROR.getType(), ApiStatusCode.SYSTEM_ERROR.getMessage());
            return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 리뷰 작성 컨트롤러}
     * @param requestDto
     */
    @PostMapping("/review")
    public ResponseEntity<ResponseJsonObject> uploadReview(
            @RequestPart(value = "imgFileList", required = false) List<MultipartFile> imgFiles,
            @RequestParam("key") ReviewUploadRequestDto requestDto) {
        // 파라미터 검증
        Map<String, String> errorsMap = checkParameterValid(requestDto);
        if (errorsMap.size() >= 1) {
            ResponseJsonObject exceptionDto = new ParamValidationException(errorsMap).getResponseJsonObject();
            return new ResponseEntity<>(exceptionDto, HttpStatus.BAD_REQUEST);
        }
        // 1. 인증된 사용자 토큰 값
        // 1-1. 인증된 사용자의 인증 객체 가져오기
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        // 1-2. 인증 객체의 유저정보 가져오기
        JWTUserDetails userDetails = (JWTUserDetails) authenticationToken.getPrincipal();
        // 2. 인코딩된 content 디코딩
        String decodedContent = CryptUtils.Base64Decoding(requestDto.getContent());

        // 3. 리뷰 생성
        Review review = new Review().builder()
                .placeId(requestDto.getPlaceId())
                .content(decodedContent)
                .stars(requestDto.getStars())
                .user(User.builder()
                        .userId(authenticationToken.getName())  // Name == userId(이메일)
                        .suid(userDetails.getSuid())
                        .said(userDetails.getSaid())
                        .build())
                .isDelete(0)
                .build();
        //  4. 이미지파일 s3 저장 (업로드할 이미지가 있는 경우에)
        List<String> uploadedImgUrls = uploadImgUrls(imgFiles);
        // 이중 체크 (db에 null로 저장되지 않는 문제
        if (CollectionUtils.isNullOrEmpty(uploadedImgUrls))
            review.setImgUrl(null);  // null
        else
            review.setImgUrl(uploadedImgUrls);
        // 5. 리뷰 업로드 서비스 호출
        Review savedReview = reviewService.uploadReview(review);

        // 6. content, imgUrl 인코딩
        String encodedContent = CryptUtils.Base64Encoding(savedReview.getContent());
        List<String> savedImgUrl = savedReview.getImgUrl();
        ArrayList<String> encodedImgUrls = null;
        if (!CollectionUtils.isNullOrEmpty(savedImgUrl)) {
            encodedImgUrls = new ArrayList<>();
            for (String imgUrl : savedImgUrl) {
                encodedImgUrls.add(CryptUtils.Base64Encoding(imgUrl));
            }
        }
        System.out.println("업로드된 reviewId : " + savedReview.getReviewId());
        // 7. responseDto 생성
        ReviewResponseDto reviewResponseDto = null;
        try {
            reviewResponseDto = new ReviewResponseDto(
                    savedReview.getReviewId(), cryptUtils.AES_Encode(savedReview.getUser().getSaid()), savedReview.getUser().getUserId(),
                    savedReview.getStars(), encodedContent,
                    encodedImgUrls,
                    StringUtil.DateTimeToString(savedReview.getCreatedAt()),
                    StringUtil.DateTimeToString(savedReview.getUpdatedAt()),
                    savedReview.getIsDelete());
        } catch (Exception ex) {
            logger.error("ReviewApiController.uploadReview Method/ Said Encoding Exception : " + ex.getMessage());
            ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR.getCode(), ApiStatusCode.SYSTEM_ERROR.getType(), ApiStatusCode.SYSTEM_ERROR.getMessage());
            return new ResponseEntity<>(resDto, HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.CREATED.getCode()).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * 바인딩된 ReviewUploadRequestDto를 검증한다.
     * @param requestDto
     */
    private Map<String, String> checkParameterValid(ReviewUploadRequestDto requestDto) {
        Map<String, String> errorsMap = new HashMap<>();
        String defaultMessage;

        // 1. stars 체크
        if (requestDto.getStars() < -1 || requestDto.getStars() > 6) {
            defaultMessage = "stars는 1 이상 5 이하의 정수로 작성해야 합니다.";
            errorsMap.put("stars", defaultMessage);
        }
        // 2. content null 체크
        if (requestDto.getContent() == null) {
            defaultMessage = "내용을 입력해야합니다.";
            errorsMap.put("content", defaultMessage);
        }
        return errorsMap;
    }

    /**
     * {@Summary 리뷰 업데이트 컨트롤러}
     * @param reviewId
     * @param imgFiles
     * @param requestDto
     */
//    @PutMapping("/reviews/{reviewId}")
    @RequestMapping(value="/reviews/{reviewId}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJsonObject> updateReview(@PathVariable Long reviewId,
                                                           @RequestPart(value = "imgFileList", required = false) List<MultipartFile> imgFiles,
                                                           @RequestParam(value = "key") ReviewUpdateRequestDto requestDto) {
        // 1. 인증된 사용자 토큰 값
        // 1-1. 인증된 사용자의 인증 객체 가져오기
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        // 1-2. 인증 객체의 유저정보 가져오기
        JWTUserDetails userDetails = (JWTUserDetails) authenticationToken.getPrincipal();
        // 2. 유효성 검증
        Review findReview = reviewService.listReview(reviewId);
        if (findReview == null) {
            return new ResponseEntity<>(new PersonIdNotFoundException().getResponseJsonObject(),
                    HttpStatus.BAD_REQUEST);
        }
        if (!isWriterCheck(findReview.getUser().getSuid(),userDetails.getSuid()))
            return new ResponseEntity<>(ResponseJsonObject.withError(ApiStatusCode.FORBIDDEN.getCode(), ApiStatusCode.FORBIDDEN.getType(), ApiStatusCode.FORBIDDEN.getMessage()), HttpStatus.FORBIDDEN);

        // 3. 리뷰 생성
        Review renewReview = new Review().builder()
                .content(CryptUtils.Base64Decoding(requestDto.getContent()))
                .stars(requestDto.getStars())
                .build();
        List<String> encodedImgUrlList = null;
        // 4. imgUrl : save or delete / 인코딩
        List<String> renewImgUrls = s3Service.saveOrDeleteImg(findReview.getImgUrl(), requestDto.getImgUrl(), imgFiles);
        if(CollectionUtils.isNullOrEmpty(renewImgUrls))
            renewReview.setImgUrl(null);
        else {
            renewReview.setImgUrl(renewImgUrls);
            for (String imgUrl : renewImgUrls)
                encodedImgUrlList.add(CryptUtils.Base64Encoding(imgUrl));
        }
        // 5. 리뷰 업데이트 서비스 호출
        Review updatedReview = reviewService.updateReview(findReview, renewReview);
        // 6. content 인코딩
        String encodedContent = CryptUtils.Base64Encoding(updatedReview.getContent());
        System.out.println(encodedContent);
        // 7. responseDto 생성
        ReviewResponseDto reviewResponseDto;
        ResponseJsonObject resDto;
        try {
            reviewResponseDto = new ReviewResponseDto(
                    updatedReview.getReviewId(), cryptUtils.AES_Encode(updatedReview.getUser().getSaid()), updatedReview.getUser().getUserId(),
                    updatedReview.getStars(), encodedContent,
                    encodedImgUrlList,
                    StringUtil.DateTimeToString(updatedReview.getCreatedAt()),
                    StringUtil.DateTimeToString(updatedReview.getUpdatedAt()),
                    updatedReview.getIsDelete());
        } catch(Exception ex) {
            logger.error("ReviewApiController.updateReview Method/ Said Encoding2 Exception : " + ex.getMessage());
            resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR.getCode(),ApiStatusCode.SYSTEM_ERROR.getType(),ApiStatusCode.SYSTEM_ERROR.getMessage());
            return new ResponseEntity<>(resDto,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 리뷰 제거 컨트롤러}
     * @param reviewId
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseJsonObject> deleteReview(@PathVariable Long reviewId) {
        // 1. 인증된 사용자 토큰 값
        // 1-1. 인증된 사용자의 인증 객체 가져오기
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        // 1-2. 인증 객체의 유저정보 가져오기
        JWTUserDetails userDetails = (JWTUserDetails) authenticationToken.getPrincipal();

        // 2. 유효성 검증
        Review findReview = reviewService.listReview(reviewId);
        if (findReview == null) {
            return new ResponseEntity<>(new PersonIdNotFoundException().getResponseJsonObject(),
                    HttpStatus.BAD_REQUEST);
        }
        if (!isWriterCheck(findReview.getUser().getSuid(), userDetails.getSuid()))
            return new ResponseEntity<>(ResponseJsonObject.withError(ApiStatusCode.FORBIDDEN.getCode(), ApiStatusCode.FORBIDDEN.getType(), ApiStatusCode.FORBIDDEN.getMessage()), HttpStatus.FORBIDDEN);

        // 3. db - 리뷰 삭제 서비스 호출
        reviewService.deleteReview(reviewId);
        // 4. s3 - 이미지파일 제거 서비스 호출
        s3Service.deleteFiles(findReview.getImgUrl());

        // 5. responseDto 생성
        return new ResponseEntity<>(ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()), HttpStatus.OK);
    }

    /** 작성자의 suid와 사용자의 suid를 비교하여 검증한다. */
    private boolean isWriterCheck(String writerSuid, String userSuid) {
        if (writerSuid.equals(userSuid))
            return true;
        return false;
    }

    private List<String> uploadImgUrls(List<MultipartFile> uploadedimgFiles) {
        List<String> uploadedImgUrls;
        if (!CollectionUtils.isNullOrEmpty(uploadedimgFiles)) {   //  content길이로 빈 파일인지 체크
            uploadedImgUrls = new ArrayList<>();
            for (MultipartFile imgFile : uploadedimgFiles)
                uploadedImgUrls.add(s3Service.uploadFile(imgFile));
            return uploadedImgUrls;
        } else
            return null;
    }
}