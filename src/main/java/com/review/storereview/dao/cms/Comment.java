package com.review.storereview.dao.cms;

import com.review.storereview.dao.BaseTimeEntity;
import com.review.storereview.dao.cust.User;
import lombok.*;
import javax.persistence.*;

@Table(name="COMMENT")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 테스트할 경우 PUBLIC으로 설정
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="COMMENT_ID")
    private Long commentId;

    @ManyToOne(fetch=FetchType.LAZY)         // Comment to Board
    @JoinColumn(name="REVIEW_ID")
    private Review review;

    @ManyToOne(fetch=FetchType.LAZY)      // Comment To User
    @JoinColumn(name="SAID")
    private User said;

    @ManyToOne(fetch=FetchType.LAZY)      // Comment To User
    @JoinColumn(name="SUID")
    private User suid;

    @Column(name = "CONTENT", length = 300)
    private String content;

    @Builder
    public Comment(Review review, User suid, User said, String content) {
        this.review = review;
        this.suid = suid;
        this.said = said;
        this.content = content;
    }

}
