package com.review.storereview.dao.cms;

import com.review.storereview.dao.BaseTimeEntity;
import com.review.storereview.dao.cust.User;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name="COMMENT")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 테스트할 경우 PUBLIC으로 설정
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(name="REVIEW")
    private Long review;

    @Column(name="SUID")
    private String suid;

    @Column(name="SAID")
    private String said;

    @Column(name = "CONTENT", length = 300)
    private String content;

    @Column(name = "USER_ID")
    private String userId;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Builder

    public Comment(Long id, Long review, String suid, String said, String content, String userId, LocalDateTime createdAt, LocalDateTime updatedAt) {
        Id = id;
        this.review = review;
        this.suid = suid;
        this.said = said;
        this.content = content;
        this.userId = userId;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
