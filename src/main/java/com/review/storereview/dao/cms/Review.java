package com.review.storereview.dao.cms;

import com.review.storereview.common.utils.StringListConverter;
import com.review.storereview.dao.BaseTimeEntity;
import lombok.*;
import javax.persistence.*;
import java.util.List;

@Table(name="REVIEW")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 테스트할 경우 PUBLIC으로 설정
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="REVIEW_ID")
    private Long reviewId;

    @OneToOne(fetch=FetchType.EAGER)      // Review To User
    @JoinColumns({@JoinColumn(name="SUID", referencedColumnName = "SUID"),
                    @JoinColumn(name="SAID", referencedColumnName = "SAID")})
    private User user;

    @Column(name="PLACE_ID", length = 20)
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
    public Review(String content) {
        this.content = content;
    }

    public String getSuid() {
        return  user.getSuid();
    }

    public void update(String content) {
        this.content = content;
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
