package com.review.storereview.config;

import com.review.storereview.common.JwtTokenProvider;
import com.review.storereview.common.enumerate.Authority;
import com.review.storereview.common.exception.handler.AuthenticationExceptionHandler;
import com.review.storereview.common.exception.handler.AuthorizationExceptionHandler;
import com.review.storereview.filter.AuthorizationCheckFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Class       : SecurityConfig
 * Author      : 조 준 희
 * Description : Security 설정 객체
 * History     : [2022-01-10] - 조 준희 - Class Create
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 3. provider
    private final JwtTokenProvider jwtTokenProvider;
    // 4. 401,403 Handler
    private final AuthenticationExceptionHandler authenticationExceptionHandler;
    private final AuthorizationExceptionHandler authorizationExceptionHandler;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    @Autowired
    public SecurityConfig( JwtTokenProvider jwtTokenProvider
    , AuthenticationExceptionHandler authenticationExceptionHandler
    , AuthorizationExceptionHandler authorizationExceptionHandler
    , AuthenticationManagerBuilder authenticationManagerBuilder
    , UserDetailsService userDetailsService) throws Exception {
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationExceptionHandler = authenticationExceptionHandler;
        this.authorizationExceptionHandler = authorizationExceptionHandler;
        this.authenticationManagerBuilder = authenticationManagerBuilder;

        // Provider 추가 설정  기본적으로 DaoAuthenticationProvider가 있음
        // Default Provider를 설정함. => Default 실패시 DaoAuthenticationProvider의 authenticate가 실행.
        authenticationManagerBuilder.authenticationProvider(jwtTokenProvider);

    }

    /**
     * 전반적인 Spring Security의 설정
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // token을 사용하는 방식이기 때문에 csrf를 disable합니다.
                .csrf().disable()
                .formLogin() .disable()
                //  예외 처리 지정
                .exceptionHandling()
                    .authenticationEntryPoint(authenticationExceptionHandler)       //401 Error Handler
                    .accessDeniedHandler(authorizationExceptionHandler)                //403 Error Handler

                // enable h2-console
                    .and()
                .headers()
                .frameOptions()
                .sameOrigin()       // 동일 도메인에서는 iframe 접근 가능

                // 세션을 사용하지 않기 때문에 STATELESS로 설정
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                /**
                 * URI별 인가 정보 셋팅.
                 */
                .and()
                .authorizeRequests()
                    .antMatchers("/authenticate").permitAll()    // 인증 절차 없이 접근 허용(로그인 관련 url)
                    .antMatchers("/api/signup").permitAll()
                    .antMatchers("/comment").hasRole(Authority.USER.getName())
                    .antMatchers("/test/tester").hasRole(Authority.USER.getName())
                    .antMatchers("/test/admin").hasRole(Authority.ADMIN.getName())
                    .anyRequest().authenticated()       // 그 외 나머지 리소스들은 무조건 인증을 완료해야 접근 가능
                .and()
                //AuthenticationFilterChain- UsernamePasswordAuthenticationFilter 전에 실행될 커스텀 필터 등록
                .addFilterBefore(new AuthorizationCheckFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
                //.apply(new JwtSecurityConfig(jwtTokenProvider));
    }
}
