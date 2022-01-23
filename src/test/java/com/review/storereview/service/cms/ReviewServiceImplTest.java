package com.review.storereview.service.cms;

import com.review.storereview.dao.cms.Review;
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
    @Mock BaseReviewRepository reviewRepo;
    @InjectMocks ReviewServiceImpl reviewService;

    public ReviewServiceImplTest(BaseReviewRepository reviewRepo) {
        this.reviewRepo = reviewRepo;
    }

    @Test
    void 리뷰_업로드() {
        // given
        List<String> imgUrl = new ArrayList<String>(Arrays.asList("http://s3-img-url-test1.com","http://s3-img-url-test2.com"));
        ReviewUploadRequestDto uploadRequestDto = new ReviewUploadRequestDto("1234", "1234567890", imgUrl);
        // when
        Review testUploadedReview = reviewService.uploadReview(uploadRequestDto);

        // verify
        Assertions.assertThat(testUploadedReview.getContent()).isEqualTo("1234567890");
        // 출력
        testUploadedReview.toString();
    }

    @Test
    void 특정리뷰_조회() {

    }
}