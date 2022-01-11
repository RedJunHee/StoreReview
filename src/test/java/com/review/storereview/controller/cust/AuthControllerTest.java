package com.review.storereview.controller.cust;

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

    String authenticateParams;
    String unAuthenticateParams;

    @Override
    protected Object controller() {
        return authController;
    }

    @BeforeEach
    public void paramSetup()  throws Exception{
        authenticateParams = "{\"id\":\"redjoon10@gmail.com\",\"password\":\"1234567\"}";
        unAuthenticateParams = "{\"id\":\"redjosson10@gmail.com\",\"password\":\"1234567\"}";
    }

    @Test
    @DisplayName("권한 필요한 API 요청시 사용자 인증 실패 ")
    @Transactional
    public void test() throws Exception
    {
        mockMvc.perform(get("/test"));
    }

    /**
     * 존재하는 유저가 로그인기능 후 token발행
     * 1. 결과값으로 HTTPSTATUS값이 200인지 확인.
     * 2. Meta값으로 code가 200인지 확인.
     * 3. 리턴 Body중 token Key가 존재하는지 확인.
     * @throws Exception
     */
    @Test
    @DisplayName("정상인증 테스트")
    @Transactional
    public void successAuthentication() throws Exception
    {
        mockMvc.perform(post("/authenticate")
                .content(authenticateParams).contentType(this.contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.meta.code",is(200)))
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
    @DisplayName("존재하지 않는 사용자 테스트")
    @Transactional
    public void failureAuthentication() throws Exception
    {
        mockMvc.perform(post("/authenticate")
                .content(unAuthenticateParams).contentType(this.contentType))
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
    @DisplayName("잘못된 AccessToken으로 접근시 오류")
    @Transactional
    public void failureAuthorization() throws Exception{
        mockMvc.perform(get("/test")
                .header("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWRqb29uMTBAZ21haWwuY29tIiwiYXV0aCI6dIlJPTEVfVVNFUiIsImRhdGEiOiJTaW1wbGVEYXRhISEiLCJleHAiOjE2NDE5NjkzODF9.vkPj9Us1YrfF3eheD-QbL-4yVy7RdbmEsOrKiLynlASr__uDbji10dtOaVUg33mWVBJ1XPvzDEIALrmNRPDWp7Q")
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
    public void successAuthorization() throws Exception{
        mockMvc.perform(get("/test")
                        .header("Authorization","Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJyZWRqb29uMTBAZ21haWwuY29tIiwiYXV0aCI6IlJPTEVfVVNFUiIsImRhdGEiOiJTaW1wbGVEYXRhISEiLCJleHAiOjE2NDE5NjkzODF9.vkPj9Us1YrfF3eheD-QbL-4yVy7RbmEsOrKiLynlASr__uDbji10dtOaVUg33mWVBJ1XPvzDEIALrmNRPDWp7Q")
                ).andExpect(status().isOk())  // Unauthorized() 401 에러
                .andExpect(result -> is("testAPI"))
        ;
    }


}