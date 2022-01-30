package com.review.storereview.dto.request;

import com.review.storereview.dao.cms.Review;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * {@Summary 리뷰 수정 요청 클래스 (DTO) }
 * INPUT : [review_id, content]
 * Author      : 문 윤 지
 * History     : [2022-01-23]
 */
@Getter
@NoArgsConstructor
public class ReviewUpdateRequestDto {
    private String content;
    private List<String> imgUrl;
    private Integer stars;

    public ReviewUpdateRequestDto(String content, List<String> imgUrl, Integer stars) {
        this.content = content;
        this.imgUrl = imgUrl;
        this.stars = stars;
    }

    // Dto에서 필요한 부분을 entity화
    public Review toEntity() {
        return new Review(
                content, imgUrl, stars);
    }
}
