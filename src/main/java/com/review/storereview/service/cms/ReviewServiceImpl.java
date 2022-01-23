package com.review.storereview.service.cms;

import com.review.storereview.common.exception.ReviewNotFoundException;
import com.review.storereview.dao.cms.Review;
import com.review.storereview.dto.request.ReviewUpdateRequestDto;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import com.review.storereview.repository.cms.BaseReviewRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl {
    private final BaseReviewRepository reviewRepository;

    public ReviewServiceImpl(BaseReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    /** {@Summary place에 해당하는 n개의 리뷰 데이터 리스트 조회 Service (2차원 리스트)} */
    public List<Review> listAllReviews(String placeId) {
        // 리뷰 데이터를 리스트화 & null 이라면 빈 컬렉션 반환
        List<Review> findReviews = Optional.ofNullable(reviewRepository.findAllByPlaceIdOrderByCreatedAtDesc(placeId))
                .orElse(Collections.emptyList());

        return findReviews;
    }

    /** {@Summary 특정 리뷰 데이터 조회 Service}*/
    public Review listReview(Long reviewId) {
        // 리뷰 데이터 조회 & null 체크
        Review findReview = Optional.ofNullable(reviewRepository.findByReviewId(reviewId))
                .orElseThrow(ReviewNotFoundException::new);

        return findReview;
    }

    /**
     * {@Summary 특정 가게의 리뷰글들의 평균 계산하여 가게 평균 구하는 Service}
     * @param findReviews
     */
    public Double AveragePlaceStars(List<Review> findReviews) {
        Double sum = .0;
        for(Review review : findReviews) {
            sum += review.getStars();
        }
        return sum / findReviews.size();
    }

    /**{@Summary 리뷰 업로드 Service} */
    @Transactional
    public Review uploadReview(ReviewUploadRequestDto reviewUploadRequestDto) {
        // 리뷰 데이터 저장
        Review savedReview = reviewRepository.save(reviewUploadRequestDto.toEntity());
        return savedReview;
    }

    /** {@Summary 리뷰 업데이트 Service} */
    @Transactional
    public Review updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto) {
        // 1. 리뷰 데이터 조회 & null 체크
        Review findReview = Optional.ofNullable(reviewRepository.findByReviewId(reviewId))
                .orElseThrow(ReviewNotFoundException::new);

        // 2. 리뷰 데이터 수정
        findReview.update(reviewUpdateRequestDto.toEntity().getContent());

        return findReview;
    }

    /**{@Summary 리뷰 데이터 제거 Service} **/
    public void deleteReview(Long reviewId) {
        // 1. 제거할 리뷰 데이터 조회 & null 체크
        Review findReview = Optional.ofNullable(reviewRepository.findByReviewId(reviewId))
                .orElseThrow(ReviewNotFoundException::new);

       // 2. 리뷰 데이터 제거
        reviewRepository.delete(findReview);
    }
}

