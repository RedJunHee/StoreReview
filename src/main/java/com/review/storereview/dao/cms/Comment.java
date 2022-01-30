package com.review.storereview.dao.cms;

import com.review.storereview.dao.BaseTimeEntity;
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
    @Column(name="COMMENT_ID")
    private Long commentId;

    @Column(name="REVIEW_ID")
    private Long reviewId;

    @OneToOne(fetch=FetchType.EAGER)      // Review To User

    @JoinColumns({@JoinColumn(name="SUID", referencedColumnName = "SUID"),
            @JoinColumn(name="SAID", referencedColumnName = "SAID")})
    private User user;

    @Column(name = "CONTENT", length = 300)
    private String content;

    @Column(name = "CREATED_AT")
    private LocalDateTime createdAt;

    @Column(name = "UPDATED_AT")
    private LocalDateTime updatedAt;

    @Column(name = "IS_DELETE")
    private Integer isDelete;


    public void setisDelete(Integer isdelete)
    {
        isDelete = isdelete;
    }

    @Builder
    public Comment(Long commentId, Long reviewId, User user, String content, String userId, LocalDateTime createdAt, LocalDateTime updatedAt
    ,Integer IsDelete) {
        this.commentId = commentId;
        this.reviewId = reviewId;
        this.user = user;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isDelete = IsDelete;
    }
}
