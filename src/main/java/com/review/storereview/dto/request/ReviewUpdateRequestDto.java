package com.review.storereview.dto.request;

import com.review.storereview.dao.cms.Review;

/**
 * TODO 리뷰 수정 요청 클래스 (DTO)
 * INPUT : [review_id, content]
 */
public class ReviewUpdateRequestDto {
    private String content;

    public ReviewUpdateRequestDto(String content) {
        this.content = content;
    }

    // Dto에서 필요한 부분을 entity화
    public Review toEntity() {
        return new Review(
                content);
    }
}
