package com.review.storereview.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class       : CommentRequestDto
 * Author      : 조 준 희
 * Description : 리뷰의 코멘트 조회 요청 DTO
 * History     : [2022-01-24] - 조 준희 - Class Create
 */
@Getter
@NoArgsConstructor
public class CommentRequestDto {
    private Integer reviewId;
    private Integer pageNo;

    @Builder
    public CommentRequestDto(Integer reviewId, Integer pageNo) {
        this.reviewId = reviewId;
        this.pageNo = pageNo;
    }

}
