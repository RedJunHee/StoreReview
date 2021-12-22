package com.review.StoreReview.domain.user;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.*;

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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="SUID", nullable = false)
    private String SUID;

    @Column(name="SAID", nullable = false)
    private String SAID;

    @Column(name="ID", nullable = false)
    private String ID;

    @Column(name="PASSWORD", nullable = false)
    private String PASSWORD;

    @Column(name="NAME", nullable = false)
    private String NAME;

    @Column(name="NICKNAME", nullable = false)
    private String NICKNAME;

    @Column(name="BIRTH_DATE", nullable = false)
    private String BIRTH_DATE;

    @Column(name="GENDER", nullable = false)
    private String GENDER;

    @Column(name="PHONE", nullable = false)
    private String PHONE;

    @Builder
    public User(String SUID, String SAID, String ID, String PASSWORD
            , String NAME, String NICKNAME, String BIRTH_DATE
            , String GENDER, String PHONE) {
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
