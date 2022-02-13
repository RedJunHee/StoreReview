package com.review.storereview.dto.request;

import com.review.storereview.dao.cms.User;
import com.review.storereview.common.enumerate.Gender;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class UserSaveRequestDto{
    private String userId;
    private String password;
    private String name;
    private String nickname;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;
    private Gender gender;
    private String phone;

    // Dto에서 필요한 부분을 entity화
    public User toEntity() {
        return User.builder()
                .userId(userId)
                .password(password)
                .name(name)
                .nickname(nickname)
                .birthDate(birthDate)
                .gender(gender)
                .phone(phone)
                .build();
    }
}
