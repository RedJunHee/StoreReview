package com.review.storereview.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class       : CommentWriteRequestDto
 * Author      : 조 준 희
 * Description : 리뷰 작성 DTO
 * History     : [2022-01-27] - 조 준희 - Class Create
 */
@Getter
@NoArgsConstructor
public class CommentWriteRequestDto {
    private Long reviewId;
    private String content;  // Base 64 Encoding

    @Builder
    public CommentWriteRequestDto(Long reviewId, String content) {
        this.reviewId = reviewId;
        this.content = content;
    }
}
