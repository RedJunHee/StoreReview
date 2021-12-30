package com.review.storereview.dto.response;

import com.review.storereview.common.enumerate.Gender;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class UserResponseDto implements BaseResponseDto {
    private String suid;
    private String said;
    private String id;
    private String name;
    private String nickname;
    private LocalDateTime birthDate;
    private Gender gender;
    private String phone;

    @Builder
    public UserResponseDto(String suid, String said, String id, String name, String nickname, LocalDateTime birthDate, Gender gender, String phone) {
        this.suid = suid;
        this.said = said;
        this.id = id;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
    }
}
