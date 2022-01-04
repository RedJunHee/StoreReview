package com.review.storereview.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure (HttpSecurity http) throws Exception {
        http.cors().disable()  // CORS(다른 도메인의 요청/응답을 성립하게 하는 방식) 설정하여 보안 강화 가능. Spring과 독립적으로 작용되는 설정. 일단 disable
                .csrf().disable()   // 디폴트인 csrf 토큰 검사 비활성화 (rest api 요청만 받으므로 csrf 토큰이 아닌 OAuth2, jwt토큰을 포함시킨다.)
                .formLogin().disable()      // 폼로그인 방식 비활성화
                .headers().frameOptions().sameOrigin(); // X-frame-Options 설정 (sameOrigin() : 동일 도메인에서는 iframe 접근 가능)
    }
}
