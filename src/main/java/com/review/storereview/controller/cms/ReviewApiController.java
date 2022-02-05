package com.review.storereview.controller.cms;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.utils.CryptUtils;
import com.review.storereview.common.utils.StringUtil;
import com.review.storereview.dao.JWTUserDetails;
import com.review.storereview.dao.cms.Review;
import com.review.storereview.dao.cms.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.ReviewDeleteRequestDto;
import com.review.storereview.dto.request.ReviewUpdateRequestDto;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import com.review.storereview.dto.response.ReviewResponseDto;
import com.review.storereview.dto.response.ReviewListResponseDto;
import com.review.storereview.service.S3Service;
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

import java.time.format.DateTimeFormatter;
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
    private final CryptUtils cryptUtils;
    private final S3Service s3Service;
    private static final String S3_END_POINT = "https://storereview-bucket.s3.us-east-2.amazonaws.com/";

    @Autowired
    public ReviewApiController(ReviewServiceImpl reviewService, CryptUtils cryptUtils, S3Service s3Service) {
        this.reviewService = reviewService;
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

        // 2. placeAvgStars 계산
        Double placeAvgStars = reviewService.AveragePlaceStars(findReviews);

        // 3. listResponseDto 생성 및 추가
        ReviewListResponseDto listResponseDto = new ReviewListResponseDto(placeAvgStars);
        for (Review review : findReviews) {
            // 3.1. content 인코딩
            String encodedContent = CryptUtils.Base64Encoding(review.getContent());
            // 3.2. responseDto 추가
            try {
                listResponseDto.addReview(
                        new ReviewResponseDto(
                                review.getReviewId(),
                                cryptUtils.AES_Encode(review.getUser().getSaid()),
                                review.getUser().getUserId(),
                                review.getStars(),
                                encodedContent,
                                review.getImgUrl(),
                                StringUtil.DateTimeToString(review.getCreatedAt()),
                                StringUtil.DateTimeToString(review.getUpdatedAt()),
                                review.getIsDelete()
                        )
                );
            } catch(Exception ex) {
                logger.error("ReviewApiController.findAllReviews Method/ Said Encoding Exception : " + ex.getMessage());
                ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR.getCode(),ApiStatusCode.SYSTEM_ERROR.getType(),ApiStatusCode.SYSTEM_ERROR.getMessage());
                return new ResponseEntity<>(resDto,HttpStatus.INTERNAL_SERVER_ERROR);
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
        // 2. content 인코딩
        String encodedContent = CryptUtils.Base64Encoding(findReview.getContent());

        // 3. responseDto 생성
        ReviewResponseDto reviewResponseDto = null;
        try {
            reviewResponseDto = new ReviewResponseDto(
                    findReview.getReviewId(), cryptUtils.AES_Encode(findReview.getUser().getSaid()), findReview.getUser().getUserId(),
                    findReview.getStars(), encodedContent,
                    findReview.getImgUrl(),
                    StringUtil.DateTimeToString(findReview.getCreatedAt()),
                    StringUtil.DateTimeToString(findReview.getUpdatedAt()),
                    findReview.getIsDelete());
        } catch(Exception ex) {
            logger.error("ReviewApiController.findOneReview Method/ Said Encoding Exception : " + ex.getMessage());
            ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR.getCode(),ApiStatusCode.SYSTEM_ERROR.getType(),ApiStatusCode.SYSTEM_ERROR.getMessage());
            return new ResponseEntity<>(resDto,HttpStatus.INTERNAL_SERVER_ERROR);
        }

        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 리뷰 작성 컨트롤러}
     * @param requestDto
     */
    @PostMapping("/review")
    public ResponseEntity<ResponseJsonObject> uploadReview(@RequestPart ("imgFileList") List<MultipartFile> imgFileList,
                                                           @RequestParam("key") ReviewUploadRequestDto requestDto) {
        System.out.println("upload review 실행 : " + requestDto.toString());
        // 1. 인증된 사용자 토큰 값
        // 1-1. 인증된 사용자의 인증 객체 가져오기
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        // 1-2. 인증 객체의 유저정보 가져오기
        JWTUserDetails userDetails = (JWTUserDetails) authenticationToken.getPrincipal();
        // 2. 인코딩된 content 디코딩
        String decodedContent = CryptUtils.Base64Decoding(requestDto.getContent());
        //  3. 이미지파일 s3 저장 (업로드할 이미지가 있는 경우에)
        List<String> uploadedImgUrlList = new ArrayList<>(imgFileList.size());
        if (imgFileList.size() >= 1) {
            imgFileList.forEach(imgFile -> {  // 프론트와 상의 후 진행 가능할 듯
                uploadedImgUrlList.add(s3Service.uploadFile(imgFile));
            });
        }

        // 4. 리뷰 생성 및 서비스 호출
        Review review = new Review().builder()
                .placeId(requestDto.getPlaceId())
                .content(decodedContent)
                .stars(requestDto.getStars())
                .imgUrl(uploadedImgUrlList)
                .user(User.builder()
                        .userId(authenticationToken.getName())  // Name == userId(이메일)
                        .suid(userDetails.getSuid())
                        .said(userDetails.getSaid())
                        .build())
                .isDelete(0)
                .build();
        Review savedReview = reviewService.uploadReview(review);

        // 5. content, imgUrl 인코딩
        String encodedContent = CryptUtils.Base64Encoding(savedReview.getContent());
        List<String> savedImgUrl = savedReview.getImgUrl();
        ArrayList<String> encodedImgFile = new ArrayList<>(savedImgUrl.size());
        if (savedImgUrl.size() >= 1) {
            savedImgUrl.forEach(imgUrl -> {
                encodedImgFile.add(CryptUtils.Base64Encoding(imgUrl));
            });
        }
        // 6. responseDto 생성
        ReviewResponseDto reviewResponseDto = null;
        try {
            reviewResponseDto = new ReviewResponseDto(
                    savedReview.getReviewId(), cryptUtils.AES_Encode(savedReview.getUser().getSaid()), savedReview.getUser().getUserId(),
                    savedReview.getStars(), encodedContent,
                    encodedImgFile,
                    StringUtil.DateTimeToString(savedReview.getCreatedAt()),
                    StringUtil.DateTimeToString(savedReview.getUpdatedAt()),
                    savedReview.getIsDelete());
        } catch(Exception ex) {
            logger.error("ReviewApiController.uploadReview Method/ Said Encoding Exception : " + ex.getMessage());
            ResponseJsonObject resDto = ResponseJsonObject.withError(ApiStatusCode.SYSTEM_ERROR.getCode(),ApiStatusCode.SYSTEM_ERROR.getType(),ApiStatusCode.SYSTEM_ERROR.getMessage());
            return new ResponseEntity<>(resDto,HttpStatus.INTERNAL_SERVER_ERROR);
        }
        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.CREATED.getCode()).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 리뷰 업데이트 컨트롤러}
     * @param reviewId
     * @param requestDto
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseJsonObject> updateReview(@PathVariable Long reviewId, @RequestPart("imgFileList") List<MultipartFile> imgFileList,
                                                           @RequestParam("key") ReviewUpdateRequestDto requestDto) {
        // 1. 인코딩된 content 디코딩 및 content 세팅
        String decodedContent = CryptUtils.Base64Decoding(requestDto.getContent());
        // 2. 인증된 사용자 토큰 값
        // 2-1. 인증된 사용자의 인증 객체 가져오기
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        // 2-2. 인증 객체의 유저정보 가져오기
        JWTUserDetails userDetails = (JWTUserDetails) authenticationToken.getPrincipal();

        // 3. 리뷰 작성자 유효성 검증
        // 3.1. 리뷰 조회 & null 체크
        Review findReview = reviewService.listReview(reviewId);
        // 3.2 검증
        if (!(findReview.getUser().getSuid().equals(userDetails.getSuid()))) {
            return new ResponseEntity<>(ResponseJsonObject.withError(ApiStatusCode.FORBIDDEN.getCode(), ApiStatusCode.FORBIDDEN.getType(), ApiStatusCode.FORBIDDEN.getMessage()), HttpStatus.FORBIDDEN);
        }
     // 4.1. 지워질 이미지 url만 남기는 로직 (제거할 이미지가 있는 경우)
        List<String> ImgUrlListFromDB = findReview.getImgUrl();
        if (!ImgUrlListFromDB.equals(requestDto.getImgUrlList())) {    // 남은 리스트가 db의 url리스트와 다르면
            requestDto.getImgUrlList().forEach(url -> {
                ImgUrlListFromDB.removeIf(dbUrl -> dbUrl.equals(url));    // 지워질 url만 남게된다.
            });
        }
        // 4.2. 파일 제거 서비스 호출
        String filename = null;
        if (ImgUrlListFromDB.size() >= 1)
            for (String deletedImgUrl : ImgUrlListFromDB) {
                filename = deletedImgUrl.replace(S3_END_POINT, "");
                s3Service.deleteFile(filename);
            }
        // 5. 추가된 이미지파일 s3 업로드 서비스 호출 (업로드할 이미지가 있는 경우에)
        if (imgFileList.size() >=1 ) {
            imgFileList.forEach(imgFile -> {
                String imgUrl = s3Service.uploadFile(imgFile);
                requestDto.getImgUrlList().add(imgUrl);
            });
        }
        // 6. 리뷰 생성
        Review renewReview = new Review().builder()
                .content(decodedContent)
                .stars(requestDto.getStars())
                .imgUrl(requestDto.getImgUrlList())
                .build();

        // 8. 리뷰 업데이트 서비스 호출
        Review updatedReview = reviewService.updateReview(findReview, renewReview);
        // 9. content, imgUrl 인코딩
        String encodedContent = CryptUtils.Base64Encoding(updatedReview.getContent());
        List<String> encodedImgUrlList = new ArrayList<>();
        if (updatedReview.getImgUrl().size() >= 1) {
            for (String imgUrl : updatedReview.getImgUrl()) {
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

        // 2.2 검증
        if (!findReview.getUser().getSuid().equals(userDetails.getSuid())) {
            return new ResponseEntity<>(ResponseJsonObject.withError(ApiStatusCode.FORBIDDEN.getCode(), ApiStatusCode.FORBIDDEN.getType(), ApiStatusCode.FORBIDDEN.getMessage()), HttpStatus.FORBIDDEN);
        }

        // 3. 리뷰 제거 서비스 호출
        reviewService.deleteReview(reviewId);
        // 4. 이미지파일 제거 서비스 호출
        String filename = null;
        List<String> imgUrlList = findReview.getImgUrl();
        if (imgUrlList.size() >= 1)
            for (String deletedImgUrl : imgUrlList) {
                filename = deletedImgUrl.replace(S3_END_POINT, "");
                s3Service.deleteFile(filename);
            };

        // 5. responseDto 생성
        return new ResponseEntity<>(ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()), HttpStatus.OK);
    }
}