package com.review.storereview.common.exception.handler;

import com.review.storereview.common.exception.ParamValidationException;
import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.dto.ResponseJsonObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * controller 전역적인 예외처리 : 에러를 한 곳에서 처리
 *
 */
@RestControllerAdvice(basePackages = "com.review.storereview.controller")   // @ControllerAdvice(스프링 빈으로 등록되어 전역적 에러를 핸들링) + json형식의 파싱이 가능
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 사용자 정의 예외

    // 파라미터 유효성 검사 문제 Exception
    @ExceptionHandler(ParamValidationException.class)
    public ResponseEntity<ResponseJsonObject> handleParamValidationException(ParamValidationException ex) {

        return new ResponseEntity<>(ex.getResponseJsonObject(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PersonAlreadyExistsException.class)   // 회원가입 시 이미 존재하는 회원이 있을 경우 호출되는 Exception
    public ResponseEntity<ResponseJsonObject> handlePersonAlreadyExistsException(PersonAlreadyExistsException ex) {
        System.out.println("GlobalExceptionHandler.handlePersonAlreadyExistsException가 호출됨.============");
        return new ResponseEntity<>(ex.getResponseJsonObject(), HttpStatus.CONFLICT);
    }

    protected ResponseEntity<Object> handleExceptionInternal(final ResponseJsonObject resDto, final HttpStatus httpStatus) {
        return new ResponseEntity<>(resDto, httpStatus);
    }
}
