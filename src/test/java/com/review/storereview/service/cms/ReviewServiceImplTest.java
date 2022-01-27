package com.review.storereview.service.cms;

import com.review.storereview.common.exception.ReviewNotFoundException;
import com.review.storereview.dao.cms.Review;
import com.review.storereview.dto.request.ReviewUpdateRequestDto;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import com.review.storereview.repository.cms.BaseReviewRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * {@Summary Service Layer 단위 테스트}
 */
@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {
    @Mock BaseReviewRepository reviewRepository;
    @InjectMocks ReviewServiceImpl reviewService;
    List<String> imgUrl = new ArrayList<String>(Arrays.asList("http://s3-img-url-test1.com","http://s3-img-url-test2.com"));
    ReviewUploadRequestDto uploadRequestDto = new ReviewUploadRequestDto("1234", "리뷰 서비스 테스트", imgUrl);

    @Test
    void 리뷰_업로드() {
        // when
        Review testUploadedReview = reviewService.uploadReview(uploadRequestDto);

        // verify
        Assertions.assertThat(testUploadedReview.getContent()).isEqualTo("1234567890");
        // 출력
        System.out.println(testUploadedReview.toString());
    }

    @Test
    void 리뷰_조회() {
        //when
        Long reviewId = 4L;
        Review findOneReview =  reviewService.listReview(reviewId);
        // verify
        Assertions.assertThat(findOneReview.getContent()).isEqualTo("리뷰 서비스 테스트");
        // 출력
        System.out.println(findOneReview.toString());
    }

    @Test
    void 가게_리뷰_전체_조회() {
        // when
        String placeId = "1234";
        List<Review> findReviews = reviewService.listAllReviews(placeId);
        for (Review r : findReviews)
            // verify
            Assertions.assertThat(r.getPlaceId()).isEqualTo(placeId);
    }

    @Test
    void 리뷰_수정() {
        List<String> updatedImgUrl = new ArrayList<String>(Arrays.asList("http://s3-img-url-test1.com"));
        ReviewUpdateRequestDto reviewUpdateRequestDto = new ReviewUpdateRequestDto("리뷰 업데이트 서비스 테스트", updatedImgUrl);

        // when : 조회
        Long reviewId = 3L;
        Review findOneReview =  reviewService.listReview(reviewId);
        // verify
        Assertions.assertThat(findOneReview.getContent()).isEqualTo("리뷰 서비스 테스트");

        // when
        Review updatedReview = reviewService.updateReview(reviewId, reviewUpdateRequestDto);
        // verify
        Assertions.assertThat(updatedReview.getContent()).isEqualTo(reviewUpdateRequestDto.toEntity().getContent());

    }

    @Test
    void 리뷰_삭제() {
        // when
        Long reviewId = 4L;
        Review findReview = reviewService.listReview(reviewId);
        reviewService.deleteReview(findReview.getReviewId());

        // 해당 리뷰가 없다면 ReviewNotFoundException을 던진다.
        ReviewNotFoundException exception = assertThrows(ReviewNotFoundException.class,
                () -> reviewService.listReview(reviewId));

        // then
        assertEquals("해당 게시물을 찾을 수 없습니다.", exception.getMessage());
    }
}