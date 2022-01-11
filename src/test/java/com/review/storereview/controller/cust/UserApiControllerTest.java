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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootTest
class UserApiControllerTest {
    @Autowired
    private BaseUserRepository userRepository;

    @Test
    public void  로그인_테스트()
    {
        String user_id = "redjoon10@gmail.com";
        String password = "dkaghghkehlsvotmdnjem";
        UserSigninRequestDto requstDto = new UserSigninRequestDto(user_id, password);

        Optional<User> user = userRepository.findByIdAndPassword(requstDto.getId(),requstDto.getPassword());

        if(user.isPresent()) {
            ResponseJsonObject resDto = new ResponseJsonObject(ApiStatusCode.OK).setData(user.get());
            Assertions.assertThat(user.get().getSuid()).isEqualTo("RE0000000001");
        }
    }

    @Transactional  // 테스트 실행 후 다시 Rollback 된다. (@Commit 붙여줄 경우 테스트 시 실행된 트랜잭션이 커밋되어 롤백되지않는다.)
    @Commit
    @Test
    public void 유저생성_조회() {
        // given
//        LocalDate date = LocalDate.of(1999, 11, 15);
        LocalDate birthDate = LocalDate.now();

        userRepository.save(User.builder()          // hibernate: 같은 KEY값은 UPDATE
                .suid("RE001341155s")
                .said("KA0223874453")
                .id("")
                .name("문")
                .nickname("moonz")
                .password("1234")
                .birthDate(birthDate)
                .gender(Gender.W)
                .phone("01012345678")
                .build());
        // given
        Optional<User> result = Optional.ofNullable(userRepository.findBySuid("RE001341155s"));
        System.out.println("조회값 : " + result.get().getSuid());
        // then
        Assertions.assertThat(result.get().getSuid()).isEqualTo("RE001341155s");
    }
}