package com.review.storereview.dto.request;

import com.review.storereview.dao.cms.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
                .userId(userId)
                .password(password)
                .build();
    }

    @Override
    public String toString() {
        return "{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
