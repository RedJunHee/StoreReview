package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.dto.ExceptionResponseDto;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

// 회원 조회 불가 에러
public class PersonNotFoundException extends RuntimeException {

    private final ApiStatusCode code = ApiStatusCode.PERSON_NOT_FOUND;
    private final ExceptionResponseDto exceptionResponseDto ;

    public PersonNotFoundException() {
        super();
        exceptionResponseDto = ExceptionResponseDto.builder().withMeta(
                ExceptionResponseDto.Meta.builder()
                        .withCode(code)
                        .build()).build();
    }

    public ExceptionResponseDto getExceptionResponseDto(){
        return exceptionResponseDto;
    }
}
