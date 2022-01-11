package com.review.storereview.service.cust;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cust.BaseUserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest
@DisplayName("UserServiceImpl 테스트")
class UserServiceImplTest {
    @Autowired
    private BaseUserRepository userRepository;
    private UserServiceImpl userService = new UserServiceImpl(userRepository);

    @Test
    @DisplayName("로그인 테스트")
    public void  로그인_테스트()
    {
        String user_id = "redjoon10@gmail.com";
        String password = "1234567";
        UserSigninRequestDto requstDto = new UserSigninRequestDto(user_id, password);

        Optional<User> user = userRepository.findByIdAndPassword(requstDto.getUserId(),requstDto.getPassword());

        if(user.isPresent()) {
            ResponseJsonObject resDto = new ResponseJsonObject(ApiStatusCode.OK).setData(user.get());
            Assertions.assertThat(user.get().getSuid()).isEqualTo("RE001341154s");
        }
    }

    @Transactional  // 테스트 실행 후 다시 Rollback 된다. (@Commit 붙여줄 경우 테스트 시 실행된 트랜잭션이 커밋되어 롤백되지않는다.)
    @Commit
    @Test
    public void 유저생성_조회() {
        // given
        LocalDate birthDate = LocalDate.of(1999, 11, 15);

        UserSaveRequestDto userSaveRequestDto = UserSaveRequestDto.builder()     // hibernate: 같은 KEY값은 UPDATE
                .suid("RE001341155s")
                .said("KA0223874453")
                .id("moonz99")
                .name("문")
                .nickname("moonz")
                .password("1234567")
                .birthDate(birthDate)
                .gender(Gender.W)
                .phone("01012345678")
                .build();

        // when
        userService.join(userSaveRequestDto);
        Optional<User> result = Optional.ofNullable(userRepository.findBySuid("RE001341155s"));
        System.out.println("조회값 : " + result.get().getSuid());
        // then
        Assertions.assertThat(result.get().getSuid()).isEqualTo("RE001341155s");
    }
}