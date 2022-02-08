package com.review.storereview.controller.cms;

import com.review.storereview.controller.TestController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import javax.transaction.Transactional;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Class       : AuthControllerTest
 * Author      : 조 준 희
 * Description : AuthController + Spring Security ( Security Filter Chain ) + JWT 인증 인가 절차의 테스트를 정의한다.
 * 인증 테스트 / 인가 테스트
 * History     : [2022-01-11] - 조 준희 - Class Create
 */

class AuthControllerTest extends AbstractControllerTest {

    @Autowired
    public AuthController authController;
    @Autowired
    public TestController testController;
    // 아이디 패스워드 모두 유효한 파라미터.
    String validAuthParam;
    String validAuthParam2;

    // 패스워드 유효하지 않는 파람
    String invalidPasswordParam;
    // 아이디 유효하지 않는 파람
    String invalidUserIdParam;
    // 아이디 패스워드 모두 유효하지 않는 파람
    String invalidAuthParam;


    @Override
    protected Object authController() {
        return authController;
    }

    @Override
    protected Object testController() {
        return testController;
    }

    @BeforeEach
    public void paramSetup()  throws Exception{
        // 테스터 계정의 비밀번호는 "test"
        validAuthParam = "{\"userId\":\"test@review.com\",\"password\":\"9F86D081884C7D659A2FEAA0C55AD015A3BF4F1B2B0B822CD15D6C15B0F00A08\"}";
        validAuthParam2 = "{\"userId\":\"local999@naver.com\",\"password\":\"4525198B45979E2D454E7EB0406D8DA1F4D7486EF68633C713B6975CC9C554E4\"}";
        invalidPasswordParam = "{\"userId\":\"test@review.com\",\"password\":\"9F86D081884C7D659333sA2F0C55AD015A3BF4F1B2B0B822CD15D6C15B0F00A08\"}";
        invalidUserIdParam = "{\"userId\":\"tessssst@review.com\",\"password\":\"9F86D081884C7D659A2FEAA0C55AD015A3BF4F1B2B0B822CD15D6C15B0F00A08\"}";
        invalidAuthParam = "{\"userId\":\"tessssst@review.com\",\"password\":\"9F86D081884C7D659A2FEAAsssdAD015A3BF4F1B2B0B822CD15D6C15B0F00A08\"}";
    }

    @Test
    @DisplayName("권한 필요한 API 요청시 사용자 인증 실패 ")
    @Transactional
    public void test() throws Exception
    {
        testControllerMockMvc.perform(get("/test/test"));
    }

    /**
     * 존재하는 유저가 로그인기능 후 token발행
     * 1. 결과값으로 HTTPSTATUS값이 200인지 확인.
     * 2. Meta값으로 code가 200인지 확인.
     * 3. 리턴 Body중 token Key가 존재하는지 확인.
     * @throws Exception
     */
    @Test
    @DisplayName("아이디, 패스워드 모두 유효함. ")
    @Transactional
    public void 아이디_패스워드_모두_유효() throws Exception
    {
        authControllerMockMvc.perform(post("/authenticate")
                .content(validAuthParam2).contentType(this.contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.statusCode",is(200)))
                .andExpect(jsonPath("$..['token']").exists());
    }


    /**
     * 인증 절차 과정으로 존재하지 않는 사용자 테스트
     *  1. 결과값으로 HTTPSTATUS값이 Unauthorized 401을 리턴해주는지 확인.
     *  2. Meta값 statusCode가 401인지 확인.
     *  3. Meta값 errorType이 "UnAuthorized"인지 확인
     * @throws Exception
     */
    @Test
    @DisplayName("아이디가 유효하지 않음.")
    @Transactional
    public void 아이디_유효하지_않음() throws Exception
    {
        authControllerMockMvc.perform(post("/authenticate")
                .content(invalidUserIdParam).contentType(this.contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.meta.statusCode",is(401))) // Json Meta.statusCode 값 비교
                .andExpect(jsonPath("$.meta.errorType",is("UnAuthorized"))); // Json Meta.errorType 값 비교
    }

    /**
     * 인증 절차 과정으로 존재하지 않는 사용자 테스트
     *  1. 결과값으로 HTTPSTATUS값이 Unauthorized 401을 리턴해주는지 확인.
     *  2. Meta값 statusCode가 401인지 확인.
     *  3. Meta값 errorType이 "UnAuthorized"인지 확인
     * @throws Exception
     */
    @Test
    @DisplayName("패스워드가 유효하지 않음.")
    @Transactional
    public void 패스워드_유효하지_않음() throws Exception
    {
        authControllerMockMvc.perform(post("/authenticate")
                        .content(invalidPasswordParam).contentType(this.contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.meta.statusCode",is(401))) // Json Meta.statusCode 값 비교
                .andExpect(jsonPath("$.meta.errorType",is("UnAuthorized"))); // Json Meta.errorType 값 비교
    }

    /**
     * 인증 절차 과정으로 존재하지 않는 사용자 테스트
     *  1. 결과값으로 HTTPSTATUS값이 Unauthorized 401을 리턴해주는지 확인.
     *  2. Meta값 statusCode가 401인지 확인.
     *  3. Meta값 errorType이 "UnAuthorized"인지 확인
     * @throws Exception
     */
    @Test
    @DisplayName("패스워드가 유효하지 않음.")
    @Transactional
    public void 아이디_패스워드_유효하지_않음() throws Exception
    {
        authControllerMockMvc.perform(post("/authenticate")
                        .content(invalidAuthParam).contentType(this.contentType))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.meta.statusCode",is(401))) // Json Meta.statusCode 값 비교
                .andExpect(jsonPath("$.meta.errorType",is("UnAuthorized"))); // Json Meta.errorType 값 비교
    }

    /**
     *  발행된 토큰을 헤더에 삽입 ( 변조된 JWT 토큰)
     *  1. 결과값으로 HTTPSTATUS값이 Unauthorized 401을 리턴해주는지 확인.
     *  2. Meta값 statusCode가 401인지 확인.
     *  3. Meta값 errorType이 "UnAuthorized"인지 확인
     * @throws Exception
     */
    @Test
    @DisplayName("잘못된 AccessToken으로 접근 시 오류")
    @Transactional
    public void 잘못된_Access토큰_and_리소스_접근_가능_요청() throws Exception{
        testControllerMockMvc.perform(get("/test/tester")
                .header("Authorization","Bearer eyJhdbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHJldmlldy5jb20iLCJhdXRoIjoiVEVTVEVSIiwiZGF0YSI6IlNpbXBsZURhdGEhISIsImV4cCI6MTY0MjA0NDY5Nn0.dYhqUs6H-BGJ6ys6BaNNhDD6dIfQRXAz0261xv2KjpCFRvFAhgdhYZHXuc5G-9zG3wu2PO-MZJzXakEjHi6TsQ")
        ).andExpect(status().isUnauthorized())  // Unauthorized() 401 에러
         .andExpect(jsonPath("$.meta.statusCode",is(401))) // Json Meta.statusCode 값 비교
         .andExpect(jsonPath("$.meta.errorType",is("UnAuthorized"))); // Json Meta.errorType 값 비교
        ;
    }

    /**
     *  발행된 토큰을 헤더에 삽입 ( 정상적인 JWT 토큰)
     *  1. 결과값으로 HTTPSTATUS값이 OK 200을 리턴해주는지 확인.
     *  2. Body값으로 "testAPI"가 출력되는지 확인.
     * @throws Exception
     */
    @Test
    @DisplayName("정상적인 AccessToken으로 접근시 오류")
    @Transactional
    public void 정상적인_Access토큰_and_리소스_접근_가능_요청() throws Exception{
        testControllerMockMvc.perform(get("/test/tester")
                        .header("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHJldmlldy5jb20iLCJhdXRoIjoiUk9MRV9URVNURVIiLCJkYXRhIjoiU2ltcGxlRGF0YSEhIiwiZXhwIjoxNjQyMDQ1MzQ3fQ.U2B7CUkGbE271u8wL_tuTTCuK4rSQUnkPI5ODTCCY75pCMFdtcsdxUNNsHoiEujmLD3xpPATQ2TJE7yk70kRgw")
                ).andExpect(status().isOk())  // 200
                .andExpect(result -> is("testAPI"))
        ;
    }

    /**
     *  발행된 토큰을 헤더에 삽입 ( 정상적인 JWT 토큰)
     *  1. 결과값으로 HTTPSTATUS값이 OK 200을 리턴해주는지 확인.
     *  2. Body값으로 "testAPI"가 출력되는지 확인.
     * @throws Exception
     */
    @Test
    @DisplayName("권한없는 리소스에 접근")
    @Transactional
    public void 권한없는_리소스_접근() throws Exception{
        testControllerMockMvc.perform(get("/test/admin")
                        .header("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHJldmlldy5jb20iLCJhdXRoIjoiUk9MRV9URVNURVIiLCJkYXRhIjoiU2ltcGxlRGF0YSEhIiwiZXhwIjoxNjQyMDQ1MzQ3fQ.U2B7CUkGbE271u8wL_tuTTCuK4rSQUnkPI5ODTCCY75pCMFdtcsdxUNNsHoiEujmLD3xpPATQ2TJE7yk70kRgw")
                ).andExpect(status().isForbidden())  // Unauthorized() 403 에러
                .andExpect(result -> is("testAPI"))
        ;
    }
    /**
     *  발행된 토큰을 헤더에 삽입 ( 정상적인 JWT 토큰)
     *  1. 결과값으로 HTTPSTATUS값이 OK 200을 리턴해주는지 확인.
     *  2. Body값으로 "testAPI"가 출력되는지 확인.
     * @throws Exception
     */
    @Test
    @DisplayName("권한있는 리소스에 접근")
    @Transactional
    public void 권한있는_리소스_접근() throws Exception{
        testControllerMockMvc.perform(get("/test/tester")
                        .header("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJ0ZXN0QHJldmlldy5jb20iLCJhdXRoIjoiUk9MRV9URVNURVIiLCJkYXRhIjoiU2ltcGxlRGF0YSEhIiwiZXhwIjoxNjQyMDQ1MzQ3fQ.U2B7CUkGbE271u8wL_tuTTCuK4rSQUnkPI5ODTCCY75pCMFdtcsdxUNNsHoiEujmLD3xpPATQ2TJE7yk70kRgw")
                ).andExpect(status().isOk())  // 200
                .andExpect(result -> is("testAPI"))
        ;
    }
}