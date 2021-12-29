package com.review.storereview.controller.cust;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cust.BaseUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@SpringBootTest
class UserApiControllerTest {

    BaseUserRepository userRepository;

    @Autowired
    public UserApiControllerTest(BaseUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Test
    public void  로그인_테스트()
    {
        String user_id = "redjoon10@gmail.com";
        String password = "dkaghghkehlsvotmdnjem";
        UserSigninRequestDto requstDto = new UserSigninRequestDto(user_id, password);

        Optional<User> user = userRepository.findByIdAndPassword(requstDto.getUserId(),requstDto.getPassword());

        ResponseJsonObject resDto = ResponseJsonObject.builder().withMeta(
                ResponseJsonObject.Meta.builder()
                        .withCode(ApiStatusCode.OK)
                        .build()).withData( user).build();

        Assertions.assertThat(user.get().getSuid()).isEqualTo("RE0000000001");

    }
}