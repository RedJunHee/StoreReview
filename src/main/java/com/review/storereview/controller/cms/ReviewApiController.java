package com.review.storereview.controller.cms;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.utils.CryptUtils;
import com.review.storereview.dao.JWTUserDetails;
import com.review.storereview.dao.cms.Review;
import com.review.storereview.dao.cms.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.ReviewUpdateRequestDto;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import com.review.storereview.dto.response.ReviewResponseDto;
import com.review.storereview.dto.response.ReviewListResponseDto;
import com.review.storereview.service.cms.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
/**
 * {@Summary 리뷰 api 요청 컨트롤러 }
 * Author      : 문 윤 지
 * History     : [2022-01-23]
 */
@RestController
public class ReviewApiController {
    private final ReviewServiceImpl reviewService;
    protected static List<String> reviewsResponseDtoList = new ArrayList<>();

    @Autowired
    public ReviewApiController(ReviewServiceImpl reviewService) {
        this.reviewService = reviewService;
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
            listResponseDto.addReview(
                    new ReviewResponseDto(
                            review.getReviewId(),
                            review.getUser().getSaid(),
                            review.getUser().getUserId(),
                            review.getStars(),
                            encodedContent,
                            review.getImgUrl(),
                            review.getCreatedAt(),
                            review.getUpdatedAt()
                    )
            );
        }
        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(listResponseDto);
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
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
                findReview.getReviewId(), findReview.getUser().getSaid(), findReview.getUser().getUserId(),
                findReview.getStars(), encodedContent,
                findReview.getImgUrl(),
                findReview.getCreatedAt(), findReview.getUpdatedAt());

        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 리뷰 작성 컨트롤러}
     * @param requestDto
     */
    @PostMapping("/review")
    public ResponseEntity<ResponseJsonObject> uploadReview(@RequestBody ReviewUploadRequestDto requestDto) {
        // 1. 인코딩된 content 디코딩
        String decodedContent = CryptUtils.Base64Decoding(requestDto.getContent());
        // 2. 인증된 사용자 토큰 값
        // 2-1. 인증된 사용자의 인증 객체 가져오기
        Authentication authenticationToken = SecurityContextHolder.getContext().getAuthentication();
        // 2-2. 인증 객체의 유저정보 가져오기
        JWTUserDetails userDetails = (JWTUserDetails) authenticationToken.getPrincipal();
        // 3. 리뷰 생성
        Review review = new Review().builder()
                .placeId(requestDto.getPlaceId())
                .content(decodedContent)
                .stars(requestDto.getStars())
                .imgUrl(requestDto.getImgUrl())
                .user(User.builder()
                        .userId(authenticationToken.getName())  // Name == userId(이메일)
                        .suid(userDetails.getSuid())
                        .said(userDetails.getSaid())
                        .build())
                .build();
        Review savedReview = reviewService.uploadReview(review);
        // 4. content 인코딩
        String encodedContent = CryptUtils.Base64Encoding(savedReview.getContent());

        // 5. responseDto 생성
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
                savedReview.getReviewId(), savedReview.getUser().getSaid(), savedReview.getUser().getUserId(),
                savedReview.getStars(), encodedContent,
                savedReview.getImgUrl(),
                savedReview.getCreatedAt(), savedReview.getUpdatedAt());
        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 리뷰 업데이트 컨트롤러}
     * @param reviewId
     * @param requestDto
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseJsonObject> updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateRequestDto requestDto) {
        // 1. 인코딩된 content 디코딩 및 content 세팅
        String decodedContent = CryptUtils.Base64Decoding(requestDto.getContent());
        // 2. 리뷰 생성
        Review review = new Review().builder()
                .content(decodedContent)
                .stars(requestDto.getStars())
                .imgUrl(requestDto.getImgUrl())
                .build();
        // 1. 리뷰 업데이트 서비스 호출
        Review updatedReview = reviewService.updateReview(reviewId, review);
        // 2. content 인코딩
        String encodedContent = CryptUtils.Base64Encoding(updatedReview.getContent());

        // 3. responseDto 생성
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
                updatedReview.getReviewId(), updatedReview.getUser().getSaid(), updatedReview.getUser().getUserId(),
                updatedReview.getStars(), encodedContent,
                updatedReview.getImgUrl(),
                updatedReview.getCreatedAt(), updatedReview.getUpdatedAt());

        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 리뷰 제거 컨트롤러}
     * @param reviewId
     */
    @DeleteMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseJsonObject> deleteReview(@PathVariable Long reviewId) {
        // 1. 리뷰 제거 서비스 로직
        reviewService.deleteReview(reviewId);

        // 2. responseDto 생성
        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }
}