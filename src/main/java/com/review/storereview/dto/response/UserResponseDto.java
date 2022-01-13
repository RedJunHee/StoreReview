package com.review.storereview.dto.response;

import com.review.storereview.common.enumerate.Gender;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDate;


@Getter
public class UserResponseDto implements BaseResponseDto {
    private String suid;
    private String said;
    private String userId;
    private String name;
    private String nickname;
    private LocalDate birthDate;
    private Gender gender;
    private String phone;

    @Builder
    public UserResponseDto(String suid, String said, String userId, String name, String nickname, LocalDate birthDate, Gender gender, String phone) {
        this.suid = suid;
        this.said = said;
        this.userId = userId;
        this.name = name;
        this.nickname = nickname;
        this.birthDate = birthDate;
        this.gender = gender;
        this.phone = phone;
    }
}
