package com.review.storereview.dto.request;

import com.review.storereview.dao.cust.User;
import com.review.storereview.common.enumerate.Gender;
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
    private Gender gender;
    private String phone;

    @Builder
    public UserSaveRequestDto(String suid, String said, String id, String password
            , String name, String nickname, LocalDateTime birthDate
            , Gender gender, String phone) {
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
                .suid(suid)
                .said(said)
                .id(id)
                .password(password)
                .name(name)
                .nickname(nickname)
                .birthDate(birthDate)
                .gender(gender)
                .phone(phone)
                .build();
    }

    /* 인코딩 후 pwd 세팅 -> 추후 SUID, SAID 인코딩 필요
    public void setPasswordEncoding(String encodingPassword) {
        this.password = encodingPassword;
     }
     */
}
