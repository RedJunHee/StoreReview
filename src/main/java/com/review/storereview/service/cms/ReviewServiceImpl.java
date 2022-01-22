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

    /** TODO place에 해당하는 n개의 리뷰 데이터 리스트 조회 Service (2차원 리스트) */
    public List<Review> listAllReviews(String placeId) {
        // 1. 리뷰 데이터를 리스트화
        Optional<List<Review>> reviews = Optional.ofNullable(reviewRepository.findAllByPlaceIdOrderByCreatedAtDesc(placeId));

        // 2. 반환
        if (!reviews.isPresent())
            return Collections.emptyList();     // 0개라면 빈 컬렉션 반환
        else
            return reviews.get();
    }

    /** TODO 특정 리뷰 데이터 조회 Service*/
    public Review listReview(Long reviewId) {
        Optional<Review> review = Optional.ofNullable(reviewRepository.findByReviewId(reviewId));

        // null 체크
        if (!review.isPresent())
            throw new ReviewNotFoundException();

        return review.get();
    }

    /**
     * TODO 특정 가게의 리뷰글들의 평균 계산하여 가게 평균 구하는 Service
     * @param findReviews
     * @return
     */
    public Double AveragePlaceStars(List<Review> findReviews) {
        Double sum = .0;
        for(Review review : findReviews) {
            sum += review.getStars();
        }
        return sum / findReviews.size();
    }

    /** TODO 리뷰 업로드 Service */
    @Transactional
    public Review uploadReview(ReviewUploadRequestDto reviewUploadRequestDto) {
        // 리뷰 데이터 저장
        Review savedReview = reviewRepository.save(reviewUploadRequestDto.toEntity());
        return savedReview;
    }

    /** TODO 리뷰 업데이트 Service */
    @Transactional
    public Review updateReview(Long reviewId, ReviewUpdateRequestDto reviewUpdateRequestDto) {
        // 1. 리뷰 데이터 조회
        Optional<Review> findReview = Optional.ofNullable(reviewRepository.findByReviewId(reviewId));

        // 1-1. null 체크
        if (!findReview.isPresent())
            throw new ReviewNotFoundException();

        // 2. 리뷰 데이터 수정
        Review review = findReview.get();
        review.update(reviewUpdateRequestDto.toEntity().getContent());

        return review;
    }

    /** TODO 리뷰 데이터 제거 Service **/
    public void deleteReview(Long reviewId) {
        // 1. 제거할 리뷰 데이터 조회
        Optional<Review> findReview = Optional.ofNullable(reviewRepository.findByReviewId(reviewId));

        // 2, null 체크
        if (!findReview.isPresent())
            throw new ReviewNotFoundException();
        else
            // 3. 리뷰 데이터 제거
            reviewRepository.delete(findReview.get());
    }
}

