package com.review.storereview.dto.request;

import com.review.storereview.dao.cust.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public class UserSigninRequestDto {
    private String id;
    private String password;

    @Builder
    public UserSigninRequestDto(String id, String password) {
        this.id = id;
        this.password = password;
    }

    public User toEntity(){
        return User.builder()
                .id(id)
                .password(password)
                .build();
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

}
