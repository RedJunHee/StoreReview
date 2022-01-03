package com.review.storereview.dto.request;

import com.review.storereview.dao.cust.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserSigninRequestDto {
    private String userId;
    private String password;

    @Builder
    public UserSigninRequestDto(String userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
                .id(userId)
                .password(password)
                .build();
    }
}
