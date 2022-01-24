package com.review.storereview.dto.response;

import com.review.storereview.dao.cust.User;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Class       : CommentResponseDto
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-24] - 조 준희 - Class Create
 */
public class CommentResponseDto {

    @Builder
    public static class comment
    {
        private Long commentId;
        private String suid;
        private String ssid;
        private String content;
        private String userId;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;

        public comment(Long commentId, String suid, String ssid, String content, String userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
            this.commentId = commentId;
            this.suid = suid;
            this.ssid = ssid;
            this.content = content;
            this.userId = userId;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }
    }

    private List<comment> comments;
    private Long totalCount;


    public CommentResponseDto(Long totalCount) {
        this.totalCount = totalCount;
    }

    public void addComment(comment comment)
    {
        this.comments.add(comment);
    }
}
