package com.review.storereview.common.exception.handler;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.common.exception.ParamValidationException;
import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.dto.ResponseJsonObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * controller 전역적인 예외처리 : 에러를 한 곳에서 처리
 *
 */
//@RestControllerAdvice(basePackages = "com.review.storereview.controller.*")   // @ControllerAdvice(스프링 빈으로 등록되어 전역적 에러를 핸들링) + json형식의 파싱이 가능
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 사용자 정의 예외

    // 파라미터 유효성 검사 문제 Exception
    @ExceptionHandler(ParamValidationException.class)
    public ResponseEntity<ResponseJsonObject> handleParamValidationException(ParamValidationException ex) {
        System.out.println("GlobalExceptionHandler.handleParamValidationException 호출됨.============");

        return new ResponseEntity<>(ex.getResponseJsonObject(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PersonAlreadyExistsException.class)
    public ResponseJsonObject handlePersonAlreadyExistsException
            (PersonAlreadyExistsException e) {
        System.out.println("GlobalExceptionHandler.handlePersonAlreadyExistsException 호출됨");
        ResponseJsonObject responseJsonObject = ResponseJsonObject.withError(
                ApiStatusCode.PERSON_ALREADY_EXISTS.getCode(),
                ApiStatusCode.PERSON_ALREADY_EXISTS.getType(),
                ApiStatusCode.PERSON_ALREADY_EXISTS.getMessage()
        );
        System.out.println(e.getResponseJsonObject());

        return e.getResponseJsonObject();
    }
/*    @ExceptionHandler(PersonAlreadyExistsException.class)   // 회원가입 시 이미 존재하는 회원이 있을 경우 호출되는 Exception
    public ResponseEntity<ResponseJsonObject> handlePersonAlreadyExistsException(PersonAlreadyExistsException ex) {
        System.out.println("GlobalExceptionHandler.handlePersonAlreadyExistsException가 호출됨.============");
        System.out.println(ex.getResponseJsonObject());
        return new ResponseEntity<>(ex.getResponseJsonObject(), HttpStatus.CONFLICT);
    }*/

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }

//    @ExceptionHandler({MethodArgumentTypeMismatchException.class})
//    public ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, WebRequest request) {
//        return new ResponseEntity<>();
//    }
}
