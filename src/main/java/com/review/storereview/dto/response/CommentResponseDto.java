package com.review.storereview.dto.response;

import com.review.storereview.common.utils.StringUtil;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

        public Long getCommentId() {
            return commentId;
        }

        public String getSuid() {
            return suid;
        }

        public String getSsid() {
            return ssid;
        }

        public String getContent() {
            return content;
        }

        public String getUserId() {
            return userId;
        }

        public String getCreatedAt() {
            return StringUtil.DateTimeToString(createdAt);
        }

        public String getUpdatedAt() {
            return StringUtil.DateTimeToString(updatedAt);
        }

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

    public List<comment> getComments() {
        return comments;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public CommentResponseDto(Long totalCount) {
        comments = new ArrayList<comment>();
        this.totalCount = totalCount;
    }

    public void addComment(comment comment)
    {
        this.comments.add(comment);
    }
}
