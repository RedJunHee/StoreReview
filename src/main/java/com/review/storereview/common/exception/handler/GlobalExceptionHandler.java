package com.review.storereview.common.exception.handler;

import com.review.storereview.common.exception.ParameterCheckFailedException;
import com.review.storereview.common.exception.PersonAlreadyExistsException;
import com.review.storereview.common.exception.PersonNotFoundException;
import com.review.storereview.common.exception.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * controller 전역적인 예외처리 : 에러를 한 곳에서 처리
 *
 */
@RestControllerAdvice   // @ControllerAdvice(전역적으로 적용되는 핸들링) + json형식의 파싱이 가능
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    // 사용자 정의 예외
    @ExceptionHandler(value = PersonNotFoundException.class)    // 사용자가 존재하지 않을 경우 예외 발생
    public ResponseEntity<ExceptionResponseDto> handlePersonNotFoundException(PersonNotFoundException ex) {

        return new ResponseEntity<ExceptionResponseDto>(ex.getExceptionResponseDto(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ParameterCheckFailedException.class)   // api 요청 시 전달하는 파라미터에 문제 발생 시 호출되는 Exception
    public ResponseEntity<ExceptionResponseDto> handleParameterCheckFailedException(ParameterCheckFailedException ex) {

        return new ResponseEntity<>(ex.getExceptionResponseDto(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = PersonAlreadyExistsException.class)   // 회원가입 시 이미 존재하는 회원이 있을 경우 호출되는 Exception
    public ResponseEntity<ExceptionResponseDto> handlePersonAlreadyExistsException(PersonAlreadyExistsException ex) {
        return new ResponseEntity<>(ex.getExceptionResponseDto(), HttpStatus.CONFLICT);
    }

}
