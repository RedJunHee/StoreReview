package com.review.StoreReview.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.*;
import java.time.LocalDateTime;

// 객체   : USER (Model)
// 설명   : CUST_DB.USER_INFO 테이블에 매핑될 Model
// 이름   : 조 준 희
// 생성   : 2021-12-21
// 기록   : [2021-12-21] - TEMP History/

@Entity
@Getter
@Table(name= "USER_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name="SUID", nullable = false)
    private String SUID;

    @Column(name="SAID", nullable = false, length = 12)
    private String SAID;

    @Column(name="ID", nullable = false, length = 12)
    private String ID;

    @Column(name="PASSWORD", nullable = false, length = 100)
    private String PASSWORD;

    // 한글 10자
    @Column(name="NAME", nullable = false, length = 10)
    private String NAME;

    //한글 10자
    @Column(name="NICKNAME", nullable = false, length = 10)
    private String NICKNAME;

    @Column(name="BIRTH_DATE", nullable = false)
    private LocalDateTime BIRTH_DATE;

    @Column(name="GENDER", nullable = false)
    private char GENDER;

    @Column(name="PHONE", nullable = false)
    private String PHONE;

    @Builder
    public User(String SUID, String SAID, String ID, String PASSWORD
            , String NAME, String NICKNAME, LocalDateTime BIRTH_DATE
            , char GENDER, String PHONE) {
        this.SUID = SUID;
        this.SAID = SAID;
        this.ID = ID;
        this.PASSWORD = PASSWORD;
        this.NAME = NAME;
        this.NICKNAME = NICKNAME;
        this.BIRTH_DATE = BIRTH_DATE;
        this.GENDER = GENDER;
        this.PHONE = PHONE;
    }
}
