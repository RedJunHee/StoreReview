package com.review.storereview.dto.request;

import com.review.storereview.dao.cms.Review;

import java.util.List;

/**
 * { Summary 리뷰 수정 요청 클래스 (DTO) }
 * INPUT : [review_id, content]
 */
public class ReviewUpdateRequestDto {
    private String content;
    private List<String> imgUrl;

    public ReviewUpdateRequestDto(String content, List<String> imgUrl) {
        this.content = content;
        this.imgUrl = imgUrl;
    }

    // Dto에서 필요한 부분을 entity화
    public Review toEntity() {
        return new Review(
                content, imgUrl);
    }
}
