package com.review.storereview.controller.cust;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.dao.cust.User;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.request.UserSaveRequestDto;
import com.review.storereview.dto.request.UserSigninRequestDto;
import com.review.storereview.repository.cust.BaseUserRepository;
import com.review.storereview.service.cust.UserServiceImpl;
import org.aspectj.lang.annotation.Before;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Commit;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

//@WebMvcTest(UserApiController.class)
@ExtendWith(SpringExtension.class)  // JUnit 프레임워크가 테스트를 실행할 시 내장된 Runner를 실행 -> bean 주입
@SpringBootTest
@DisplayName("UserApiController 테스트")
class UserApiControllerTest {

    @Autowired private BaseUserRepository userRepository;
    private UserServiceImpl userService = new UserServiceImpl(userRepository);

//    private MockMvc mvc;
//    @MockBean private UserServiceImpl userService;

//    @BeforeEach
//    public void setUp() {
//        mvc = MockMvcBuilders.standaloneSetup(new UserApiController(userService))
//                .addFilters(new CharacterEncodingFilter("UTF-8", true))
//                .build();
//    }
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

//    @Transactional  // 테스트 실행 후 다시 Rollback 된다. (@Commit 붙여줄 경우 테스트 시 실행된 트랜잭션이 커밋되어 롤백되지않는다.)
//    @Commit
//    @Test
//    @DisplayName("회원가입 테스트")
//    public void 유저생성_조회() throws Exception {    // mvc.perform() -> throws Exception
//        // given
//        final LocalDateTime birthDate = LocalDateTime.of(1999, 11, 15, 0, 0);
//        given(userService.join(any()))
//                .willReturn(
//                        User.builder()
//                                .suid("RE001341155s")
//                                .said("KA0223874453")
//                                .id("banan99")
//                                .name("문윤지")
//                                .nickname("moonz")
//                                .password("12345678")
//                                .birthDate(birthDate)
//                                .gender(Gender.W)
//                                .phone("01012345678")
//                                .build());
//
//        // when
//        final ResultActions actions =
//                mvc.perform(
//                        post("/api/signup")
//                                .contentType(MediaType.APPLICATION_JSON)    // 미디어타입 설정
//                                .accept(MediaType.APPLICATION_JSON)
//                                .characterEncoding("UTF-8")
//                                .content(
//                                        "{"
//                                                + " \"suid\" : \"RE001341155s\", "
//                                                + " \"said\" : \"KA0223874453\", "
//                                                + " \"id\" : \"banan99\", "
//                                                + " \"name\" : \"문윤지\", "
//                                                + " \"nickname\" : \"moonz\", "
//                                                + " \"password\" : \"12345678\", "
//                                                + " \"birthDate\" : \"1999-11-15T00:00\", "
//                                                + " \"gender\" : \"W\", "
//                                                + " \"phone\" : \"01012345678\", "
//                                                + "}"));
//
//        // then
//        actions
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("suid").value("RE001341155s"))
//                .andExpect(jsonPath("id").value("banan99"))
//                .andExpect(jsonPath("gender").value("W"));
//    }

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

        userService.join(userSaveRequestDto);
        // given
        Optional<User> result = Optional.ofNullable(userRepository.findBySuid("RE001341155s"));
        System.out.println("조회값 : " + result.get().getSuid());
        // then
        Assertions.assertThat(result.get().getSuid()).isEqualTo("RE001341155s");
    }

}

