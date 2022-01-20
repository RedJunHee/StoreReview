package com.review.storereview.repository.cust;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.dto.request.UserSigninRequestDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@DisplayName("User Repository Test")
class BaseUserRepositoryTest {
    @Autowired
    private BaseUserRepository userRepository;

    @Test
    @DisplayName("로그인 테스트")
    public void  로그인_테스트()
    {
        String user_id = "redjoon10@gmail.com";
        String password = "1234567";
        UserSigninRequestDto requstDto = new UserSigninRequestDto(user_id, password);

        Optional<User> user = userRepository.findByUserIdAndPassword(requstDto.getUserId(),requstDto.getPassword());

        if(user.isPresent()) {
            ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK).setData(user.get());
            Assertions.assertThat(user.get().getSuid()).isEqualTo("RE001341154s");
        }
    }

    @Test
    @DisplayName("회원가입 레파지토리 테스트")
    public void 회원가입_테스트() {
        LocalDate birthDate = LocalDate.of(1999, 11, 15);
        UserSaveRequestDto userSaveRequestDto = UserSaveRequestDto.builder()     // hibernate: 같은 KEY값은 UPDATE
                .suid("RE001341155s").said("KA0223874453").userId("moonz99").name("문").nickname("moonz").password("1234567")
                .birthDate(birthDate).gender(Gender.W).phone("01012345678")
                .build();
        userRepository.save(userSaveRequestDto.toEntity());
        Optional<User> result = Optional.ofNullable(userRepository.findBySuid("RE001341155s"));
        System.out.println("조회값 : " + result.get().getSuid());

        Assertions.assertThat(result.get().getSuid()).isEqualTo("RE001341155s");
    }
}