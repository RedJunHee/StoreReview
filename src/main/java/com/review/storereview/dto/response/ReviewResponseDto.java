package com.review.storereview.dto.response;

import com.review.storereview.dao.cust.User;

import java.time.LocalDateTime;
import java.util.List;

public class ReviewResponseDto {

    private List<ReviewResponseDto> reviewsResponseDtoList;

    // List< [REVIEW_ID, SAID, USER_ID, STARS, CONTENT, IMG_URL, CREATED_AT, UPDATED_AT]>
    private Long reviewId;
    private User said;
    private User userId;
    private Integer stars;
    private String content;
    private List<String> imgUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Double placeAvgStar;

    // 기본 생성자
    public ReviewResponseDto(Long reviewId, User said, User userId, Integer stars, String content, List<String> imgUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.reviewId = reviewId;
        this.said = said;
        this.userId = userId;
        this.stars = stars;
        this.content = content;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // 특정 가게의 전체 리뷰에 대한 생성자 (+placeAvgStar)
    public ReviewResponseDto(Long reviewId, User said, User userId, Integer stars, String content, List<String> imgUrl, LocalDateTime createdAt, LocalDateTime updatedAt, Double placeAvgStar) {
        this.reviewId = reviewId;
        this.said = said;
        this.userId = userId;
        this.stars = stars;
        this.content = content;
        this.imgUrl = imgUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.placeAvgStar = placeAvgStar;
    }

    // 리스트로 리뷰 데이터 여러개 전달할 시
    public void setPlaceAvgStar(Double placeAvgStar) {
        this.placeAvgStar = placeAvgStar;
    }

}
