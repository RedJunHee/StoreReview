package com.review.storereview.repository.cms;

import com.review.storereview.dao.cms.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BaseReviewRepository extends JpaRepository<Review, Long> {
    public List<Review> findAllByPlaceIdOrderByCreatedAtDesc(String placeId);
    public Review findByReviewId(Long reviewId);
}
