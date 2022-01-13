package com.review.storereview.dao.cms;

import com.review.storereview.dao.BaseTimeEntity;
import com.review.storereview.dao.cust.User;
import lombok.*;
import javax.persistence.*;

@Table(name="REVIEW")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 테스트할 경우 PUBLIC으로 설정
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REVIEW_ID")
    private Long reviewId;

    @ManyToOne(fetch=FetchType.LAZY)      // Review To User
    @JoinColumn(name="SAID")
    private User said;

    @ManyToOne(fetch=FetchType.LAZY)      // Review To User
    @JoinColumn(name="SUID")
    private User suid;

    @Column(name = "STARS", nullable = false)
    private double stars;

    @Column(name = "CONTENT", nullable = false, length = 1000)
    private String content;

    @Column(name = "IMG_URL", length = 45)
    private String imgUrl;

    @Builder
    public Review (User suid, User said, double stars, String content, String imgUrl) {
        this.suid = suid;
        this.said = said;
        this.stars = stars;
        this.content = content;
        this.imgUrl = imgUrl;
    }

}
