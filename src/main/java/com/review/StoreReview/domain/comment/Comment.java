package com.review.StoreReview.domain.comment;

import com.review.StoreReview.common.BaseTimeEntity;
import com.review.StoreReview.domain.review.Review;
import com.review.StoreReview.domain.user.User;
import lombok.*;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 테스트할 경우 PUBLIC으로 설정
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)         // Comment to Board
    @JoinColumn(name="review_id")
    private Review review;

    @ManyToOne(fetch=FetchType.LAZY)     // Comment to User
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "content", length = 300)
    private String content;

    @Builder
    public Comment(Review review, User user, String content) {
        this.review = review;
        this.user = user;
        this.content = content;
    }

}
