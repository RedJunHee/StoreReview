package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.dto.ExceptionResponseDto;

/**
 * api 요청 시 전달하는 파라미터에 문제 발생 시 호출되는 Exception
 * error_code : [400,"ParameterCheckFailed","문법상 또는 파라미터 오류가 있어서 서버가 요청사항을 처리하지 못함."]
 */
public class ParameterCheckFailedException extends RuntimeException{
    private final ApiStatusCode error_code = ApiStatusCode.PARAMETER_CHECK_FAILED;
    private final ExceptionResponseDto exceptionResponseDto ;

    public ExceptionResponseDto getExceptionResponseDto(){
        return exceptionResponseDto;
    }

    public ParameterCheckFailedException() {
        exceptionResponseDto = new ExceptionResponseDto(error_code);
    }

    public ParameterCheckFailedException(String message) {
        super(message);
        exceptionResponseDto = new ExceptionResponseDto(error_code,message);
    }
}
