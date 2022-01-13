package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.dto.ExceptionResponseDto;

/**
 * Class       : CustomAuthenticationException
 * Author      : 조 준 희
 * Description : Spring Security JWT 인증 과정에서 생기는 ExceptionHandler에서 사용되어지는 객체.
 * ExceptionHandler = com.review.storereview.common.exception.handler.AuthenticationExceptionHandler.java
 * History     : [2022-01-10] - 조 준희 - Class Create
 */
public class CustomAuthenticationException   {
    private final ApiStatusCode errorStatusCode = ApiStatusCode.UNAUTHORIZED;
    private final ExceptionResponseDto exceptionResponseDto;

    public ExceptionResponseDto getExceptionResponseDto(){
        return exceptionResponseDto;
    }
    

    public CustomAuthenticationException() {
        exceptionResponseDto = ExceptionResponseDto.createMetaDto(errorStatusCode);
    }

    public CustomAuthenticationException(String message) {
        exceptionResponseDto =  ExceptionResponseDto.createMetaMessageDto(errorStatusCode,message);
//        exceptionResponseDto = ExceptionResponseDto.createMetaMapDto(errorCode, errorMap);
    }

}
