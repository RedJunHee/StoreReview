package com.review.storereview.dto.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
/**
 * {@Summary 여러 개의 Review 조회 API 응답 객체 }
 * Class       : ReviewListResponseDto
 * Author      : 문 윤 지
 * History     : [2022-01-31]
 */
@Getter
public class ReviewFindListResponseDto {
    private List<ReviewFindResponseDto> reviewsResponseDtoList;
    private Double placeAvgStar;

    public ReviewFindListResponseDto(Double placeAvgStar) {
        reviewsResponseDtoList = new ArrayList<>(); // 초기화
        this.placeAvgStar = placeAvgStar;
    }

    public void addReview(ReviewFindResponseDto reviewDto)
    {
        this.reviewsResponseDtoList.add(reviewDto);
    }

}
