package com.review.storereview.common;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;


/** Class       : AppConfig (Configuration)
 *  Author      : 조 준 희
 *  Description : Review 서비스에서 사용되어지는 Bean 객체를 관리함.
 *  History     : [2022-01-03] - Temp
 */
@Configuration
public class AppConfig {

    @Bean  // 어떤 암호화방식 사용할 것인지 빈 등록
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // 기본은  BCryptPasswordEncoder 방식
    }

    @Bean   // ObjectMapper (Aop) 객체의 생성 비용이 큰 객체이므로 Bean으로 생성 및 관리
    public ObjectMapper objectMapper()
    {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);   // Java 객체를 JSON으로 Serialize할 때 null값은 제외
        return  objectMapper.registerModule(new JavaTimeModule());
    }
}
