package com.review.storereview.controller.cms;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dao.cms.Review;
import com.review.storereview.dao.cms.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.ReviewUpdateRequestDto;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import com.review.storereview.dto.response.ReviewResponseDto;
import com.review.storereview.repository.cms.BaseUserRepository;
import com.review.storereview.service.cms.ReviewServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class ReviewApiController {
    private final ReviewServiceImpl reviewService;
    private final BaseUserRepository userRepository;
    private List<ReviewResponseDto> reviewsResponseDtoList;

    @Autowired
    public ReviewApiController(ReviewServiceImpl reviewService, BaseUserRepository userRepository) {
        this.reviewService = reviewService;
        this.userRepository = userRepository;
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

        // 3. responseDto 생성 및 리스트화
        for (Review review : findReviews) {
            // 3.1. userId 조회
//            User userId = userService.listUserIdBySuid(review.getSuid());
            List<Object[]> resultUserId = userRepository.findUserIdBySuid(review.getSuid());
            Object[] objects = resultUserId.get(0);
            User userId = (User) objects[0];

            // 3.2. responseDto 생성
            ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
                    review.getReviewId(),
                    review.getUser().getSaid(),
                    userId.getUserId(),
                    review.getStars(),
                    review.getContent(),
                    review.getImgUrl(),
                    review.getCreatedAt(),
                    review.getUpdatedAt()
            );
            // 3.3. placeAvgStar 세팅
            reviewResponseDto.setPlaceAvgStar(placeAvgStars);

            // 3.4. 리스트에 추가
            reviewsResponseDtoList.add(reviewResponseDto);
        }

        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(reviewsResponseDtoList);
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
        User userId = reviewService.listUserIdBySuid(findReview.getSuid());

        // 2. responseDto 생성
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
                findReview.getReviewId(), findReview.getUser().getSaid(), userId.getUserId(),
                findReview.getStars(), findReview.getContent(),
                findReview.getImgUrl(),
                findReview.getCreatedAt(), findReview.getUpdatedAt());

        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 리뷰 작성 컨트롤러}
     * @param reviewUploadRequestDto
     */
    @PostMapping("/review")
    public ResponseEntity<ResponseJsonObject> uploadReview(@RequestBody ReviewUploadRequestDto reviewUploadRequestDto) {
        // 1. 업로드 서비스 로직 (리뷰 업로드 - userId 조회)
        Review savedReview = reviewService.uploadReview(reviewUploadRequestDto);
        User userId = reviewService.listUserIdBySuid(savedReview.getSuid());

        // 2. responseDto 생성
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
                savedReview.getReviewId(), savedReview.getUser().getSaid(), userId.getUserId(),
                savedReview.getStars(), savedReview.getContent(),
                savedReview.getImgUrl(),
                savedReview.getCreatedAt(), savedReview.getUpdatedAt());

        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(reviewResponseDto);
        return new ResponseEntity<>(resDto, HttpStatus.OK);
    }

    /**
     * {@Summary 리뷰 업데이트 컨트롤러}
     * @param reviewId
     * @param reviewUpdateRequestDto
     */
    @PutMapping("/reviews/{reviewId}")
    public ResponseEntity<ResponseJsonObject> updateReview(@PathVariable Long reviewId, @RequestBody ReviewUpdateRequestDto reviewUpdateRequestDto) {
        // 1. 리뷰 업데이트 서비스 로직
        Review updatedReview = reviewService.updateReview(reviewId, reviewUpdateRequestDto);
        User userId = reviewService.listUserIdBySuid(updatedReview.getSuid());

        // 2. responseDto 생성
        ReviewResponseDto reviewResponseDto = new ReviewResponseDto(
                updatedReview.getReviewId(), updatedReview.getUser().getSaid(), userId.getUserId(),
                updatedReview.getStars(), updatedReview.getContent(),
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