package com.review.storereview.repository.cms;

import com.review.storereview.dao.cms.Review;
import com.review.storereview.dao.cms.User;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * {@Summary Review Repository 테스트 코드 }
 * Class       : BaseReviewRepositoryTest
 * Author      : 문 윤 지
 * History     : [2022-01-23]
 */
//@ExtendWith(SpringExtension.class)
//@DataJpaTest   // JPA 관련된 설정만 로드, @Transactional 내장, @Entity 스캔
//@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@SpringBootTest
class BaseReviewRepositoryTest {
    @Autowired BaseReviewRepository reviewRepository;
    Integer stars = 4;

    @Test
    @DisplayName("리뷰글이 DB에 잘 저장되는지 확인")
    void saveReview() {
        // given
        ReviewUploadRequestDto requestDto = new ReviewUploadRequestDto("1234", "테스트 내용", stars);
        Review review = new Review().builder()
                .placeId(requestDto.getPlaceId())
                .content(requestDto.getContent())
                .stars(requestDto.getStars())
                .imgUrl(null)
                .user(User.builder()
                        .suid("testSUID")
                        .said("testSAID")
                        .build())
                .build();
        // when
        Review savedReview = reviewRepository.save(review);
        // then
//        Assertions.assertThat(review).isSameAs(savedReview);
        Assertions.assertThat(review.getPlaceId()).isEqualTo(savedReview.getPlaceId());
        Assertions.assertThat(savedReview.getReviewId()).isNotNull();
        Assertions.assertThat(reviewRepository.count()).isEqualTo(1);
    }

    @Test
    @DisplayName("저장된 리뷰들이 제대로 조회되는지 확인")
    void findReview() {
        // given
        ReviewUploadRequestDto requestDto = new ReviewUploadRequestDto("1234", "테스트 내용", stars);
        Review review = new Review().builder()
                .placeId(requestDto.getPlaceId())
                .content(requestDto.getContent())
                .stars(requestDto.getStars())
                .imgUrl(null)
                .user(User.builder()
                        .suid("testSUID")
                        .said("testSAID")
                        .build())
                .isDelete(0)
                .build();
        // when
        Review savedReview1 = reviewRepository.save(review);
        Review savedReview2 = reviewRepository.save(review);
        // when
        List<Review> findReviews = reviewRepository.findAllByPlaceIdAndIsDeleteIsOrderByCreatedAtDesc(savedReview1.getPlaceId(), savedReview1.getIsDelete());
        // then
        Assertions.assertThat(reviewRepository.count()).isEqualTo(2);
        Assertions.assertThat(findReviews.size()).isEqualTo(2);
        Assertions.assertThat(findReviews.get(0).getPlaceId()).isEqualTo(savedReview2.getPlaceId());
        Assertions.assertThat(findReviews.get(1).getImgUrl()).isEqualTo(savedReview1.getImgUrl());
    }
}