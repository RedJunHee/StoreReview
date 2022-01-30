package com.review.storereview.dto.response;


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
public class CommentDeleteResponseDto {
    private Long commentId;

    @Builder
    public CommentDeleteResponseDto(Long commentId){
        this.commentId = commentId;
    }
}
