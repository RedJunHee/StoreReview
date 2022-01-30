package com.review.storereview.dto.response;

import com.review.storereview.common.utils.StringUtil;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class       : CommentResponseDto
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-24] - 조 준희 - Class Create
 */
@Getter
public class CommentListResponseDto {

    @Getter
    @Builder
    public static class comment
    {
        private Long commentId;
        private String said;
        private String content;
        private String userId;
        private String createdAt;
        private String updatedAt;



        public comment(Long commentId, String said, String content, String userId, String createdAt, String updatedAt) {
            this.commentId = commentId;
            this.said = said;
            this.content = content;
            this.userId = userId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    private List<comment> comments;
    private Long totalCount;


    public CommentListResponseDto(Long totalCount) {
        comments = new ArrayList<comment>();
        this.totalCount = totalCount;
    }

    public void addComment(comment comment)
    {
        this.comments.add(comment);
    }
}
