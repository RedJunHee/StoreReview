package com.review.StoreReview.domain.review;

import com.review.StoreReview.common.BaseTimeEntity;
import com.review.StoreReview.domain.user.User;
import lombok.*;
import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 테스트할 경우 PUBLIC으로 설정
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="review_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)      // Review To User
    @JoinColumn(name="user_id")
    private User user;

    @Column(name = "stars", nullable = false)
    private double stars;

    @Column(name = "content", nullable = false, length = 1000)
    private String content;

    @Column(name = "img_url", length = 45)
    private String imgUrl;

    @Builder
    public Review (User user, double stars, String content, String imgUrl) {
        this.user = user;
        this.stars = stars;
        this.content = content;
        this.imgUrl = imgUrl;
    }

}
