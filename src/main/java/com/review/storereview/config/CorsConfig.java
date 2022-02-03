package com.review.storereview.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Class       : CorsConfig
 * Author      : 조 준 희
 * Description : Cors 허용 출처 설정
 * History     : [2022-02-03] - 조 준희 - Class Create
 */
@Configuration
public class CorsConfig  implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry){
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:3000");
    }
}
