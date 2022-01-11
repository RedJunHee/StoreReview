package com.review.storereview.controller.cust;

import com.review.storereview.common.enumerate.Gender;
import com.review.storereview.common.exception.ParamValidationException;
import com.review.storereview.dao.cust.User;
import com.review.storereview.service.cust.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.CharacterEncodingFilter;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(SpringExtension.class)  // JUnit 프레임워크가 테스트를 실행할 시 내장된 Runner를 실행 -> bean 주입
@DisplayName("UserApiController 테스트")
@WebMvcTest(UserApiController.class)
class UserApiControllerTest {

    private MockMvc mvc;
    @MockBean private UserServiceImpl userService;

    @BeforeEach
    public void setUp() {   // UserApiController를 MockMvc 객체로 만든다.
        mvc = MockMvcBuilders.standaloneSetup(new UserApiController(userService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // charset을 UTF-8로 설정 (option)
                .build();
    }

    /**
     * MockHttpServletResponse:
     *            Status = 200
     *     Error message = null
     *           Headers = [Content-Type:"application/json;charset=UTF-8"]
     *      Content type = application/json;charset=UTF-8
     *              Body = {"meta":{"code":200},"data":null}
     *     Forwarded URL = null
     *    Redirected URL = null
     *           Cookies = []
     * @throws Exception
     */
    @Test
    @DisplayName("회원가입 Controller 테스트")
    public void 회원가입_컨트롤러테스트() throws Exception {    // mvc.perform() -> throws Exception
        // given
        final LocalDate birthDate = LocalDate.of(1999, 11, 15);
        given(userService.join(any()))
                .willReturn(
                        User.builder()
                                .suid("DE001341155s")
                                .said("KA0223874453")
                                .id("banan99")
                                .name("문윤지")
                                .nickname("moonz")
                                .password("12345678")
                                .birthDate(birthDate)
                                .gender(Gender.W)
                                .phone("01012345678")
                                .build());

        // when
        final ResultActions actions =
                mvc.perform(        // perform() : MockMvcRequestBuilders를 통해서 구현한 Request를 테스트
                        post("/user/signup")
                                .contentType(MediaType.APPLICATION_JSON)    // 미디어타입 설정
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(
                                        "{"
                                                + " \"suid\" : \"DE001341155s\", "
                                                + " \"said\" : \"KA0223874453\", "
                                                + " \"id\" : \"banan99\", "
                                                + " \"name\" : \"문윤지\", "
                                                + " \"nickname\" : \"moonz\", "
                                                + " \"password\" : \"12345678\", "
                                                + " \"birthDate\" : \"1999-11-15T00:00\", "
                                                + " \"gender\" : \"W\", "
                                                + " \"phone\" : \"01012345678\" "
                                                + "}"));
        actions
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.code").value(200));     // response 검증
    }

    // 중복 회원가입 테스트
    @Test
    @DisplayName("중복 회원가입 Controller 테스트")
    public void 중복회원가입_컨트롤러테스트() throws Exception {
        // given
        final LocalDate birthDate = LocalDate.of(1999, 11, 15);
        given(userService.join(any()))
                .willReturn(
                        User.builder()
                                .suid("RE001341155s")
                                .said("KA0223874453")
                                .id("banan99")
                                .name("문윤지")
                                .nickname("moonz")
                                .password("12345678")
                                .birthDate(birthDate)
                                .gender(Gender.W)
                                .phone("01012345678")
                                .build());

        // when
        org.assertj.core.api.Assertions.assertThatThrownBy( () ->
                mvc.perform(
                                    post("/user/signup")
                                            .contentType(MediaType.APPLICATION_JSON)
                                            .accept(MediaType.APPLICATION_JSON)
                                            .characterEncoding("UTF-8")
                                            .content(
                                                    "{"
                                                            + " \"suid\" : \"RE001341155s\", "
                                                            + " \"said\" : \"KA0223874453\", "
                                                            + " \"id\" : \"banan99\", "
                                                            + " \"name\" : \"문윤지\", "
                                                            + " \"nickname\" : \"moonz\", "
                                                            + " \"password\" : \"12345678\", "
                                                            + " \"birthDate\" : \"1999-11-15T00:00\", "
                                                            + " \"gender\" : \"W\", "
                                                            + " \"phone\" : \"01012345678\" "
                                                            + "}"))
                        .andExpect(status().isOk()))
                        .hasCause(new ParamValidationException("회원가입 api 호출 중 에러"));
                }
}
