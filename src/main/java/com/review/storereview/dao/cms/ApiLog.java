package com.review.storereview.dao.cms;

import lombok.*;
import javax.persistence.*;
import java.time.LocalDateTime;

/** Class       : Api_Log (Model)
 *  Author      : 조 준 희
 *  Description : LOG_DB.API_LOG 테이블에 매핑되어지는 JPA 객체모델
 *  History     : [2022-01-03] - Temp
 */
@Table(name="API_LOG")
@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)  // 테스트할 경우 PUBLIC으로 설정
public class ApiLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="LOG_ID")
    private Long logId;

    @Column(name="SUID", nullable = false, length = 12)
    private String suid;

    @Column(name="SAID", nullable = false, length = 12)
    private String said;

    @Column(name="DATE")
    private LocalDateTime date;

    @Column(name="API_NAME", nullable = false, length = 20)
    private  String apiName;

    @Column(name="API_STATUS")
    private char apiStatus;

    @Column(name="API_DESC", length = 8000)
    private  String apiDesc;

    @Column(name="PROCESS_TIME")
    private double processTime;

    @Builder
    public ApiLog(String suid, String said, LocalDateTime date, String apiName, char apiStatus, String apiDesc, double processTime) {
        this.suid = suid;
        this.said = said;
        this.date = date;
        this.apiName = apiName;
        this.apiStatus = apiStatus;
        this.apiDesc = apiDesc;
        this.processTime = processTime;
    }

}
