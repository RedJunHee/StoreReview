package com.review.storereview.dto.request;


import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Class       : CommentUpdateRequestDto
 * Author      : 조 준 희
 * Description : 리뷰 작성 DTO
 * History     : [2022-01-27] - 조 준희 - Class Create
 */
@Getter
@NoArgsConstructor
public class CommentUpdateRequestDto {
    private Long commentId;
    private String content; // Base 64 Encoding

    @Builder
    public CommentUpdateRequestDto(Long commentId, String content){
        this.commentId = commentId;
        this.content = content;
    }
}
