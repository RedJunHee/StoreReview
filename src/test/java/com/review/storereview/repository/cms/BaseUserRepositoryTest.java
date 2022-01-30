package com.review.storereview.repository.cms;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.dao.cms.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cms.BaseUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Optional;

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

        // jwt를 통해 전달된 암호화&인코딩된 suid를 복호화&디코딩 후 설정하고 저장해야하기 때문에 User로 회원정보 입력
        User user = User.builder()
                .suid("SI0000000001").said("RV0000000001")
                .userId("moonz99").name("문").nickname("moonz").password("1234567")
                .birthDate(birthDate).gender(Gender.W).phone("01012345678")
                .build();

        User savedUser = userRepository.save(user);

        Assertions.assertThat(savedUser.getNickname()).isEqualTo("moonz");
    }
}