package com.review.storereview.dao.cms;

import com.review.storereview.common.utils.StringListConverter;
import com.review.storereview.dao.BaseTimeEntity;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Table(name="REVIEW")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PUBLIC)  // 테스트할 경우 PROTECTED-> PUBLIC으로 설정
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REVIEW_ID")
    private Long reviewId;

    @OneToOne(fetch=FetchType.EAGER)      // Review To User
    @JoinColumns({@JoinColumn(name="SUID", referencedColumnName = "SUID"),
                    @JoinColumn(name="SAID", referencedColumnName = "SAID")})
    private User user;      // SUID, SAID 필드를 User 필드로 통일 후 조인

    @Column(name="PLACE_ID", length = 20, nullable = false)
    private String placeId;

    @Column(name = "STARS", nullable = false)
    private Integer stars;

    @Column(name = "CONTENT", nullable = false, length = 1000)
    private String content;

    @Column(name = "IMG_URL", length = 45)
    @Convert(converter = StringListConverter.class)
    private List<String> imgUrl;

    @Builder
    public Review (User user, String placeId, Integer stars, String content, List<String> imgUrl) {
        this.user = user;
        this.placeId = placeId;
        this.stars = stars;
        this.content = content;
        this.imgUrl = imgUrl;
    }

    // ReviewUpdateRequestDto에서 필요
    public Review(String content, List<String> imgUrl, Integer stars) {
        this.content = content;
        this.imgUrl = imgUrl;
        this.stars = stars;
    }

    public String getSuid() {
        return  user.getSuid();
    }

    public void update(String content, List<String> imgUrl, Integer stars) {
        this.content = content;
        this.imgUrl = imgUrl;
        this.stars = stars;
    }

    // for Test
    @Override
    public String toString() {
        return "Review{" +
                "reviewId=" + reviewId +
                ", said=" + user.getSaid() +
                ", suid=" + user.getSuid() +
                ", placeId='" + placeId + '\'' +
                ", stars=" + stars +
                ", content='" + content + '\'' +
                ", imgUrl=" + imgUrl +
                '}';
    }
}
