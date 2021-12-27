package com.review.StoreReview.web.rest.controller.dto;

import com.review.StoreReview.domain.CUST.User;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;

@Getter
public class UserSaveRequestDto {
    private String suid;
    private String said;
    private String id;
    private String password;
    private String name;
    private String nickname;
    private LocalDateTime birthDate;
    private char gender;
    private String phone;

    @Builder
    public UserSaveRequestDto(String suid, String said, String id, String password
            , String name, String nickname, LocalDateTime birthDate
            , char gender, String phone) {
        this.suid = suid;
        this.said = said;
        this.id = id;
        this.password = password;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
    }

    // Dto에서 필요한 부분을 entity화
    public User toEntity() {
        return User.builder()
                .SUID(suid)
                .SAID(said)
                .ID(id)
                .PASSWORD(password)
                .NAME(name)
                .NICKNAME(nickname)
                .BIRTH_DATE(birthDate)
                .GENDER(gender)
                .PHONE(phone)
                .build();
    }

}
