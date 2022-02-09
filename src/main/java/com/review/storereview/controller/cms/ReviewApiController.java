package com.review.storereview.controller.cms;

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
    private static final String S3_END_POINT = "https://storereview-bucket.s3.us-east-2.amazonaws.com/";

    @Autowired
    public ReviewApiController(ReviewServiceImpl reviewService, CommentService commentService, CryptUtils cryptUtils, S3Service s3Service) {
        this.reviewService = reviewService;
        this.commentService = commentService;
        this.cryptUtils = cryptUtils;
        this.s3Service = s3Service;
    }

    /**
     * {@Summary 특정 가게에 대한 전체 리뷰 조회 컨트롤러}
     *
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
            List<String> encodedImgUrlList = new ArrayList<>();
            if (!Objects.isNull(review.getImgUrl())) {
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
     *
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
        List<String> encodedImgUrlList = new ArrayList<>();

        if (!Objects.isNull(findReview.getImgUrl())) {
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
     *
     * @param requestDto
     */
    @PostMapping("/review")
    public ResponseEntity<ResponseJsonObject> uploadReview(@RequestPart(value = "imgFileList", required = false) List<MultipartFile> imgFileList,
                                                           @RequestParam("key") ReviewUploadRequestDto requestDto) {
        System.out.println("upload review 실행 : " + requestDto.toString());
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
        List<String> uploadedImgUrlList = new ArrayList<>();
        // TODO postman으로 테스트 시, 비어있어도 null, Empty로 처리하지 않음.
        if (!Objects.isNull(imgFileList)) {   //  content길이로 빈 파일인지 체크
            imgFileList.forEach(imgFile -> {
                uploadedImgUrlList.add(s3Service.uploadFile(imgFile));
            });
            review.setImgUrl(uploadedImgUrlList);
        }

        // 5. 리뷰 업로드 서비스 호출
        Review savedReview = reviewService.uploadReview(review);

        // 6. content, imgUrl 인코딩
        String encodedContent = CryptUtils.Base64Encoding(savedReview.getContent());
        List<String> savedImgUrl = savedReview.getImgUrl();
        ArrayList<String> encodedImgUrl = new ArrayList<>();
        if (!Objects.isNull(savedImgUrl)) {
            savedImgUrl.forEach(imgUrl -> {
                encodedImgUrl.add(CryptUtils.Base64Encoding(imgUrl));
            });
        }

        // 7. responseDto 생성
        int commentNum = 0;
        ReviewResponseDto reviewResponseDto = null;
        try {
            reviewResponseDto = new ReviewResponseDto(
                    savedReview.getReviewId(), cryptUtils.AES_Encode(savedReview.getUser().getSaid()), savedReview.getUser().getUserId(),
                    savedReview.getStars(), encodedContent,
                    encodedImgUrl,
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
     * {@Summary 리뷰 업데이트 컨트롤러}
     * @param reviewId
     * @param imgFileList
     * @param requestDtoStr
     */
//    @PutMapping("/reviews/{reviewId}")
    @RequestMapping(value="/reviews/{reviewId}", method = RequestMethod.PUT)
    public ResponseEntity<ResponseJsonObject> updateReview(@PathVariable Long reviewId,
                                                           @RequestPart(value = "imgFileList", required = false) List<MultipartFile> imgFileList,
                                                           @RequestParam(value = "key") String requestDtoStr) throws JsonProcessingException {
        // 1. 인증된 사용자 토큰 값
        // 1-1. 인증된 사용자의 인증 객체 가져오기
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        // 1-2. 인증 객체의 유저정보 가져오기
        JWTUserDetails userDetails = (JWTUserDetails) authenticationToken.getPrincipal();
        // 2. 리뷰 작성자 유효성 검증
        // 2.1. 리뷰 조회 & null 체크
        Review findReview = reviewService.listReview(reviewId);
        if (findReview == null) {
            return new ResponseEntity<>(new PersonIdNotFoundException().getResponseJsonObject(),
                    HttpStatus.BAD_REQUEST);
        }
        // 2.2 유효성 검증
        if (!(findReview.getUser().getSuid().equals(userDetails.getSuid()))) {
            return new ResponseEntity<>(ResponseJsonObject.withError(ApiStatusCode.FORBIDDEN.getCode(), ApiStatusCode.FORBIDDEN.getType(), ApiStatusCode.FORBIDDEN.getMessage()), HttpStatus.FORBIDDEN);
        }
        // 3. ObjectMapping으로 String -> Dto 변환
        ObjectMapper objectMapper = new ObjectMapper()
                .registerModule(new SimpleModule());
        ReviewUpdateRequestDto requestDto = objectMapper.readValue(requestDtoStr, new TypeReference<ReviewUpdateRequestDto>() {
        });
        // 4. 리뷰 생성
        Review renewReview = new Review().builder()
                .content(CryptUtils.Base64Decoding(requestDto.getContent()))
                .stars(requestDto.getStars())
                .build();
        // 5. 제거되지 않은 url 체크
        List<String> renewImgUrlList = new ArrayList<>();
        System.out.println("전달받은  imgUrl null인지 체크 : " +Objects.isNull(requestDto.getImgUrl()));
        if (!Objects.isNull(requestDto.getImgUrl())) {
            System.out.println("requestDto.getImgUrl가 안 비어서 실행");
            for (String imgUrl : requestDto.getImgUrl()) {
                renewImgUrlList.add(imgUrl);
            }
        }

        // 5. 추가된 이미지파일 s3 업로드 서비스 호출 (업로드할 이미지가 있는 경우에)
        // TODO postman으로 테스트 시, 비어있지 않아도 null, Empty로 처리하지 않음.
        if (!Objects.isNull(imgFileList)) {
            // 5.2. s3 업로드 및 url 추가
            for (MultipartFile imgFile : imgFileList) {
                String imgUrl = s3Service.uploadFile(imgFile);
                System.out.println(imgUrl);
                renewImgUrlList.add(imgUrl);    // 기존 urlList에 업로드된 urlList 추가
            }
        }

        renewReview.setImgUrl(renewImgUrlList);
        System.out.println(renewImgUrlList.size());

        // 7. 제거할 이미지 제거
        // 7.1. 제거할 이미지 url만 남기는 로직 (제거할 이미지가 있는 경우)
        List<String> ImgUrlListFromDB = findReview.getImgUrl();
        if (!Objects.isNull(ImgUrlListFromDB)) {  // db에 url이 있다면 작업 진행 (없다면 애초에 제거할 이미지없으니 pass)
            if (!Objects.isNull(requestDto.getImgUrl())) // 남은 url이 있다면 (제거된 이미지가 있거나 없는 경우)
                    requestDto.getImgUrl().forEach(url -> {
                        ImgUrlListFromDB.removeIf(dbUrl -> dbUrl.equals(url));    // db의 url리스트와 다른지 비교하면서 같으면  제거. 남은 url은 제거할 url
                    });
            System.out.println(ImgUrlListFromDB.size());
        }
        // 7.2. 파일 제거 서비스 호출
        String fileName;
        if (!Objects.isNull(ImgUrlListFromDB))
            for (String deletedImgUrl : ImgUrlListFromDB) {
                fileName = deletedImgUrl.replace(S3_END_POINT, "");
                s3Service.deleteFile(fileName);
                System.out.println(fileName + "제거 완료");
            }

        // 8. 리뷰 업데이트 서비스 호출
        Review updatedReview = reviewService.updateReview(findReview, renewReview);
        // 9. content, imgUrl 인코딩
        String encodedContent = CryptUtils.Base64Encoding(updatedReview.getContent());
        List<String> encodedImgUrlList = new ArrayList<>();
        if (!Objects.isNull(updatedReview.getImgUrl())) {
//            encodedImgUrlList  = new ArrayList<>(renewImgUrlList.size());
            for (String imgUrl :renewImgUrlList) {
                encodedImgUrlList.add(CryptUtils.Base64Encoding(imgUrl));
            }
        }
        //10. responseDto 생성
        ReviewResponseDto reviewResponseDto = null;
        ResponseJsonObject resDto = null;
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

        // 2. 리뷰 작성자 유효성 검증
        // 2.1. 리뷰 조회 & null 체크
        Review findReview = reviewService.listReview(reviewId);
        if (findReview == null) {
            return new ResponseEntity<>(new PersonIdNotFoundException().getResponseJsonObject(),
                    HttpStatus.BAD_REQUEST);
        }
        // 2.2 유효성 검증
        if (!findReview.getUser().getSuid().equals(userDetails.getSuid())) {
            return new ResponseEntity<>(ResponseJsonObject.withError(ApiStatusCode.FORBIDDEN.getCode(), ApiStatusCode.FORBIDDEN.getType(), ApiStatusCode.FORBIDDEN.getMessage()), HttpStatus.FORBIDDEN);
        }

        // 3. 리뷰 제거 서비스 호출
        reviewService.deleteReview(reviewId);
        // 4. 이미지파일 제거 서비스 호출
        String fileName = null;
        List<String> imgUrlList = findReview.getImgUrl();
        if (!Objects.isNull(imgUrlList))
            for (String deletedImgUrl : imgUrlList) {
                fileName = deletedImgUrl.replace(S3_END_POINT, "");
                s3Service.deleteFile(fileName);
            };

        // 5. responseDto 생성
        return new ResponseEntity<>(ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()), HttpStatus.OK);
    }
}