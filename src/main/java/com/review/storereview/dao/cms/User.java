package com.review.storereview.dao.cms;

import com.review.storereview.common.enumerate.Gender;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;

/** Class       : User (Model)
 *  Author      : 조 준 희
 *  Description : CUST_DB.USER_INFO 테이블에 매필될 Model
 *  History     : [2021-12-21] - TEMP
 */
@Table(name= "USER_INFO")
@Getter
@Entity
@IdClass(UserId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User implements Serializable {
    @Id
    @Column(name="SUID", nullable = false)
    private String suid;

    @Id
    @Column(name="SAID", nullable = false, length = 12)
    private String said;

    @Column(name="USER_ID", nullable = false, length = 20, unique = true)
    private String userId;

    @NotBlank
    @Column(name="PASSWORD", nullable = false, length = 100)
    private String password;

    // 한글 10자
    @NotBlank
    @Column(name="NAME", nullable = false, length = 10)
    private String name;

    //한글 10자
    @Column(name="NICKNAME", nullable = false, length = 10)
    private String nickname;

    @Column(name="BIRTH_DATE", nullable = false)
    private LocalDate birthDate;

    @Column(name="GENDER", nullable = false)
    @Enumerated(EnumType.STRING)
    private Gender gender;

    @Column(name="PHONE", nullable = false)
    private String phone;

    @Builder
    public User(String suid, String said, String userId, String password
            , String name, String nickname, LocalDate birthDate
            , Gender gender, String phone) {
        this.suid = suid;
        this.said = said;
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
    }

    public void setSuid(String suid) {
        this.suid = suid;
    }

    public void setSaid(String said) {
        this.said = said;
    }

    public void setPassword(String password) { this.password = password; }
}
