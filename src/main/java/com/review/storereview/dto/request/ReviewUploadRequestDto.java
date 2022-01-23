package com.review.storereview.dto.request;

import com.review.storereview.dao.cms.Review;
import lombok.Builder;

import java.util.List;

/**
 * 리뷰 작성 요청 클래스 (DTO)
 * INPUT : [place_id, content]
 */
public class ReviewUploadRequestDto {
    private String placeId;
    private List<String> imgUrl;
    private String content;

    @Builder
    public ReviewUploadRequestDto(String placeId, String content, List<String> imgUrl) {
        this.placeId = placeId;
        this.content = content;
    }

    // Dto에서 필요한 부분을 entity화
    public Review toEntity() {
        return Review.builder()
                .placeId(placeId)
                .content(content)
                .build();
    }
}
