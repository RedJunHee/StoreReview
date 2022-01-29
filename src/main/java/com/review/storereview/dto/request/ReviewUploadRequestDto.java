package com.review.storereview.dto.request;

import com.review.storereview.dao.cms.Review;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * {@Summary 리뷰 작성 요청 클래스 (DTO) }
 * INPUT : [placeId, imgUrl, content, stars]
 * Author      : 문 윤 지
 * History     : [2022-01-23]
 */
@Getter
public class ReviewUploadRequestDto {
    private String placeId;
    private List<String> imgUrl;
    private String content;
    private Integer stars;

    @Builder
    public ReviewUploadRequestDto(String placeId, String content, Integer stars,  List<String> imgUrl) {
        this.placeId = placeId;
        this.content = content;
        this.stars = stars;
        this.imgUrl = imgUrl;
    }

    // Dto에서 필요한 부분을 entity화
    public Review toEntity() {
        return Review.builder()
                .placeId(placeId)
                .content(content)
                .stars(stars)
                .imgUrl(imgUrl)
                .build();
    }
}
