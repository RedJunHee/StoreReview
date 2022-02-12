package com.review.storereview.service.cms;

import com.amazonaws.util.CollectionUtils;
import com.review.storereview.common.exception.ParamValidationException;
import com.review.storereview.common.exception.ReviewNotFoundException;
import com.review.storereview.dao.cms.Review;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.repository.cms.BaseReviewRepository;
import com.review.storereview.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * {@Summary Review Service Layer }
 * Class       : ReviewServiceImpl
 * Author      : 문 윤 지
 * History     : [2022-01-23]
 */
@Service
public class ReviewServiceImpl {

    private final BaseReviewRepository baseReviewRepository;

    @Autowired
    public ReviewServiceImpl(BaseReviewRepository baseReviewRepository) {
        this.baseReviewRepository = baseReviewRepository;
    }

    /** {@Summary place에 해당하는 n개의 리뷰 데이터 리스트 조회 Service (2차원 리스트)} */
    public List<Review> listAllReviews(String placeId) {
        // TODO 해당하는 placeId가 없을 경우 throw Error 해야하나?
        // 리뷰 데이터를 리스트화 & null 이라면 빈 컬렉션 반환
        List<Review> findReviews = Optional.ofNullable(baseReviewRepository.findAllByPlaceIdAndIsDeleteIsOrderByCreatedAtDesc(placeId, 0))
                .orElse(Collections.emptyList());

        return findReviews;
    }

    /** {@Summary 특정 리뷰 데이터 조회 Service}*/
    public Review listReview(Long reviewId) {
        // 리뷰 데이터 조회 & null 체크
        Review findReview = baseReviewRepository.findByReviewIdAndIsDeleteIs(reviewId, 0);
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
    public Review uploadReview(Review review) {
        // 리뷰 데이터 저장
        return baseReviewRepository.save(review);
    }

    /** {@Summary 리뷰 업데이트 Service} */
    @Transactional
    public Review updateReview(Review findReview, Review renewReview) {
        findReview.update(renewReview.getContent(), renewReview.getStars(), renewReview.getImgUrl());
        return findReview;
    }

    /**{@Summary 리뷰 데이터 제거 Service} **/
    @Transactional
    public void deleteReview(Long reviewId) {
        // 1. 제거할 리뷰 데이터 조회 & null 체크
        Review findReview = Optional.ofNullable(baseReviewRepository.findByReviewIdAndIsDeleteIs(reviewId, 0))
                .orElseThrow(ReviewNotFoundException::new);

        // 2. isDelete 업데이트 (서비스 상 제거)
        findReview.updateIsDelete(1);
    }
}

