package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.dto.ExceptionResponseDto;

import java.util.Map;

/**
 * api 요청 시 전달하는 파라미터에 문제 발생 시 호출되는 Exception
 * error_code : [400,"ParameterCheckFailed","문법상 또는 파라미터 오류가 있어서 서버가 요청사항을 처리하지 못함."]
 */
public class ParamValidationException extends RuntimeException{
//    private Map<String, Object> errorMap;       // 파라미터 에러 메시지 (1개 이상의 파라미터에서 발생할 수 있음)
    private final ApiStatusCode errorStatusCode = ApiStatusCode.PARAMETER_CHECK_FAILED;
    private final ExceptionResponseDto exceptionResponseDto ;

    public ExceptionResponseDto getExceptionResponseDto(){
        return exceptionResponseDto;
    }

    public ParamValidationException() {
        exceptionResponseDto = ExceptionResponseDto.createMetaDto(errorStatusCode);
    }

//    public ParamValidationException(String message, Map<String, Object> errorMap) {
    public ParamValidationException(String message) {
        super(message);
        exceptionResponseDto =  ExceptionResponseDto.createMetaMessageDto(errorStatusCode,message);
//        exceptionResponseDto = ExceptionResponseDto.createMetaMapDto(errorCode, errorMap);
    }
}