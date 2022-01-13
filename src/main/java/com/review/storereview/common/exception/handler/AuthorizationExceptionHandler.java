package com.review.storereview.common.exception.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.storereview.common.exception.CustomAuthorizationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Class       : AuthorizationExceptionHandler
 * Author      : 조 준 희
 * Description : SpringSecurity 인가 확인 중 인가 절차 실패시 Exception Handle
 *             - com.review.storereview.config.SecurityConfig.java에 설정되어있음
 * History     : [2022-01-10] - 조 준희 - Class Create
 */
@Component
public class AuthorizationExceptionHandler implements AccessDeniedHandler {

    private ObjectMapper om ;

    @Autowired
    public AuthorizationExceptionHandler(ObjectMapper om) {
        this.om = om;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ServletOutputStream out = response.getOutputStream();
        om.writeValue(out,new CustomAuthorizationException(accessDeniedException.getMessage()).getExceptionResponseDto());
        out.flush();
    }

}
