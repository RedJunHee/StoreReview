package com.review.storereview.repository.cms;

import com.review.storereview.dao.cms.Review;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DataJpaTest    // JPA 관련된 설정만 로드, @Transactional 내장, @Entity 스캔
class BaseReviewRepositoryTest {
    @Autowired BaseReviewRepository reviewRepository;
    List<String> imgUrl = new ArrayList<String>(Arrays.asList("http://s3-img-url-test1.com","http://s3-img-url-test2.com"));

    @Test
    @DisplayName("리뷰글이 DB에 잘 저장되는지 확인")
    void saveReview() {
        // given
        Review review = new ReviewUploadRequestDto("1234", "테스트 내용", imgUrl).toEntity();
        // when
        Review savedReview = reviewRepository.save(review);
        // then
//        Assertions.assertThat(review).isSameAs(savedReview);
        Assertions.assertThat(review.getPlaceId()).isEqualTo(savedReview.getPlaceId());
        Assertions.assertThat(savedReview.getReviewId()).isNotNull();
        Assertions.assertThat(reviewRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("저장된 리뷰가 제대로 조회되는지 확인")
    void findReview() {
        // given
        Review savedReview1 = reviewRepository.save(new ReviewUploadRequestDto("1234", "테스트 내용1", imgUrl).toEntity());
        Review savedReview2 = reviewRepository.save(new ReviewUploadRequestDto("1234", "테스트 내용2", imgUrl).toEntity());
        // when
        List<Review> findReviews = reviewRepository.findAllByPlaceIdOrderByCreatedAtDesc(savedReview1.getPlaceId());
        // then
        Assertions.assertThat(reviewRepository.count()).isEqualTo(2);
        Assertions.assertThat(findReviews.size()).isEqualTo(2);
        Assertions.assertThat(findReviews.get(0).getPlaceId()).isEqualTo(savedReview2.getPlaceId());
        Assertions.assertThat(findReviews.get(1).getImgUrl()).isEqualTo(savedReview1.getImgUrl());
    }
}