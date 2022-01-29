package com.review.storereview.controller.cms;

import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.controller.TestController;
import com.review.storereview.service.cms.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockFilterConfig;
import org.springframework.security.config.BeanIds;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.DelegatingFilterProxy;

import javax.servlet.ServletException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith(MockitoExtension.class)  // JUnit 프레임워크가 테스트를 실행할 시 내장된 Runner를 실행 -> bean 주입
@DisplayName("UserApiController 테스트")
//@WebMvcTest(UserApiController.class)      // @SpringBootTest와 함께 사용  X
class UserApiControllerTest extends AbstractControllerTest{
    private MockMvc mvc;
    @MockBean private UserServiceImpl userService;  // 목 객체

    @Autowired
    public AuthController authController;
    @Autowired
    public TestController testController;
    @MockBean private WebApplicationContext context;

    @Override
    protected Object authController() {
        return authController;
    }
    @Override
    protected Object testController() {
        return testController;
    }

    @BeforeEach
    public void setUp() {   // UserApiController를 MockMvc 객체로 만든다.
        mvc = MockMvcBuilders.standaloneSetup(new UserApiController(userService))
                .addFilters(new CharacterEncodingFilter("UTF-8", true)) // charset을 UTF-8로 설정 (option)
                .build();
        DelegatingFilterProxy delegatingFilterProxy = new DelegatingFilterProxy();
        try {
            delegatingFilterProxy.init(new MockFilterConfig(context.getServletContext(), BeanIds.SPRING_SECURITY_FILTER_CHAIN));
        } catch (ServletException e) {
            e.printStackTrace();
        }
        authControllerMockMvc = MockMvcBuilders.standaloneSetup(authController())
                // Body 데이터 한글 안깨지기 위한 인코딩 필터 설정.
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .addFilter(delegatingFilterProxy)
                .alwaysDo(print())
                .build();

        testControllerMockMvc = MockMvcBuilders.standaloneSetup(testController())
                // Body 데이터 한글 안깨지기 위한 인코딩 필터 설정.
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .addFilter(delegatingFilterProxy)
                .alwaysDo(print())
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

        final ResultActions actions =
                mvc.perform(        // perform() : MockMvcRequestBuilders를 통해서 구현한 Request를 테스트
                        post("/api/signup")
                                .contentType(MediaType.APPLICATION_JSON)    // 미디어타입 설정
                                .accept(MediaType.APPLICATION_JSON)
                                .characterEncoding("UTF-8")
                                .content(
                                        "{"
                                                + " \"userId\" : \"@naver.com\", "
                                                + " \"name\" : \"문윤지\", "
                                                + " \"nickname\" : \"moonz\", "
                                                + " \"password\" : \"moonmoon00!\", "
                                                + " \"birthDate\" : \"1999-11-15\", "
                                                + " \"gender\" : \"W\", "
                                                + " \"phone\" : \"01012345678\" "
                                                + "}"));
        actions
                .andDo(print())
                .andExpect(status().isOk())      // HttpStatus.OK(200)
                .andExpect(content().contentType("application/json;charset=utf-8"))     // contentType 검증
                .andExpect(jsonPath("$.meta.statusCode").value(400));     // response 검증
    }

    // 중복 회원가입 테스트
    @Test
    @DisplayName("중복 회원가입 Controller 테스트")
    public void 중복회원가입_컨트롤러테스트() throws Exception {
        // given
        final LocalDate birthDate = LocalDate.of(1999, 11, 15);
        userService.join(any());

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
                                                            + " \"userId\" : \"banan99\", "
                                                            + " \"name\" : \"문윤지\", "
                                                            + " \"nickname\" : \"moonz\", "
                                                            + " \"password\" : \"12345678\", "
                                                            + " \"birthDate\" : \"1999-11-15\", "
                                                            + " \"gender\" : \"W\", "
                                                            + " \"phone\" : \"01012345678\" "
                                                            + "}"))
                        .andExpect(status().isOk()))
                        .hasCause(new PersonAlreadyExistsException());
                }
}
