package com.review.StoreReview.domain.log;

import com.review.StoreReview.domain.user.User;
import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 테스트할 경우 PUBLIC으로 설정
public class Log {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)    // Log to User
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime date;

    @Column(name="api_name", nullable = false, length = 20)
    private  String apiName;

    @Column(name="api_status")
    private char apiStatus;

    @Column(name="api_desc", length = 100)
    private  String apiDesc;

    @Column(name="process_time")
    private double processTime;

    @Builder
    public Log(User user, LocalDateTime date, String apiName, char apiStatus, String apiDesc, double processTime) {
        this.user = user;
        this.date = date;
        this.apiName = apiName;
        this.apiStatus = apiStatus;
        this.apiDesc = apiDesc;
        this.processTime = processTime;
    }
}
