package com.review.storereview.controller.cust;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cust.BaseUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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

    @Transactional  // 테스트 실행 후 다시 Rollback 된다. (@Commit 붙여줄 경우 테스트 시 실행된 트랜잭션이 커밋되어 롤백되지않는다.)
    @Commit
    @Test
    public void 유저생성_조회() {
        // given
//        LocalDate date = LocalDate.of(1999, 11, 15);
        LocalDateTime birthDate = LocalDateTime.now();

        userRepository.save(User.builder()          // 같은 KEY값을 UPDATE
                .SUID("DE001341155s")       // RE001341155s
                .SAID("KA0223874453")
                .ID("moonz99")
                .NAME("문")
                .NICKNAME("moonz")
                .PASSWORD("1234")
                .BIRTH_DATE(birthDate)
                .GENDER(Gender.W)
                .PHONE("01012345678")
                .build());
        // given
        Optional<User> result = Optional.ofNullable(userRepository.findBySuid("DE001341155s"));
        System.out.println("조회값 : " + result.get().getSuid());
        // then
        Assertions.assertThat(result.get().getSuid()).isEqualTo("DE001341155s");
    }
}