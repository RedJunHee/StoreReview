package com.review.storereview.dao.cust;

import com.review.storereview.common.enumerate.Gender;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.*;
import java.time.LocalDateTime;

/** Class       : User (Model)
 *  Author      : 조 준 희
 *  Description : CUST_DB.USER_INFO 테이블에 매필될 Model
 *  History     : [2021-12-21] - TEMP
 */

@Entity
@Getter
@Table(name= "USER_INFO")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id
    @Column(name="SUID", nullable = false)
    private String suid;

    @Column(name="SAID", nullable = false, length = 12)
    private String said;

    @Column(name="USER_ID", nullable = false, length = 12)
    private String id;

    @Column(name="PASSWORD", nullable = false, length = 100)
    private String password;

    // 한글 10자
    @Column(name="NAME", nullable = false, length = 10)
    private String name;

    //한글 10자
    @Column(name="NICKNAME", nullable = false, length = 10)
    private String nickname;

    @Column(name="BIRTH_DATE", nullable = false)
    private LocalDateTime birthDate;

    @Column(name="GENDER", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name="PHONE", nullable = false)
    private String phone;

    @Builder
    public User(String SUID, String SAID, String ID, String PASSWORD
            , String NAME, String NICKNAME, LocalDateTime BIRTH_DATE
            , Gender GENDER, String PHONE) {
        this.suid = SUID;
        this.said = SAID;
        this.id = ID;
        this.password = PASSWORD;
        this.name = NAME;
        this.nickname = NICKNAME;
        this.birthDate = BIRTH_DATE;
        this.gender = GENDER;
        this.phone = PHONE;
    }
}
