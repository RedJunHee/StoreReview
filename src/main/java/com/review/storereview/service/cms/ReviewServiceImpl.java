package com.review.storereview.service.cms;

import com.review.storereview.common.exception.ReviewNotFoundException;
import com.review.storereview.common.utils.CryptUtils;
import com.review.storereview.dao.JWTUserDetails;
import com.review.storereview.dao.cms.Review;
import com.review.storereview.dao.cms.User;
import com.review.storereview.dto.request.ReviewUpdateRequestDto;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import com.review.storereview.repository.cms.BaseReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewServiceImpl {

    private final BaseReviewRepository baseReviewRepository;
    @Autowired
    public ReviewServiceImpl(BaseReviewRepository baseReviewRepository) {
        this.baseReviewRepository = baseReviewRepository;
    }

    /** {@Summary place에 해당하는 n개의 리뷰 데이터 리스트 조회 Service (2차원 리스트)} */
    public List<Review> listAllReviews(String placeId) {
        // 리뷰 데이터를 리스트화 & null 이라면 빈 컬렉션 반환
        List<Review> findReviews = Optional.ofNullable(baseReviewRepository.findAllByPlaceIdOrderByCreatedAtDesc(placeId))
                .orElse(Collections.emptyList());

        return findReviews;
    }

    /** {@Summary 특정 리뷰 데이터 조회 Service}*/
    public Review listReview(Long reviewId) {
        // 리뷰 데이터 조회 & null 체크
        Review findReview = Optional.ofNullable(baseReviewRepository.findByReviewId(reviewId))
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
    public Review uploadReview(Review review) {
        // 리뷰 데이터 저장
        return baseReviewRepository.save(review);
    }

    /** {@Summary 리뷰 업데이트 Service} */
    @Transactional
    public Review updateReview(Long reviewId, ReviewUpdateRequestDto updateRequestDto) {
        // 1. 인코딩된 content 디코딩
        String decodedContent = CryptUtils.Base64Decoding(updateRequestDto.getContent());

        // 2. 리뷰 데이터 조회 & null 체크
        Review findReview = Optional.ofNullable(baseReviewRepository.findByReviewId(reviewId))
                .orElseThrow(ReviewNotFoundException::new);

        // 3. 리뷰 데이터 수정
        findReview.update(decodedContent, updateRequestDto.toEntity().getImgUrl()
            , updateRequestDto.toEntity().getStars());

        return findReview;
    }

    /**{@Summary 리뷰 데이터 제거 Service} **/
    public void deleteReview(Long reviewId) {
        // 1. 제거할 리뷰 데이터 조회 & null 체크
        Review findReview = Optional.ofNullable(baseReviewRepository.findByReviewId(reviewId))
                .orElseThrow(ReviewNotFoundException::new);

       // 2. 리뷰 데이터 제거
        baseReviewRepository.delete(findReview);
    }

}

