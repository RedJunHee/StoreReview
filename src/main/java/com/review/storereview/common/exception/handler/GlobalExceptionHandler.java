package com.review.storereview.common.exception.handler;

import com.review.storereview.common.exception.PersonNotFoundException;
import com.review.storereview.common.exception.dto.ExceptionResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * controller 전역적인 예외처리 : 에러를 한 곳에서 처리
 *
 */
@RestControllerAdvice   // @ControllerAdvice(전역적으로 적용되는 핸들링) + json형식의 파싱이 가능
public class GlobalExceptionHandler {
    // 사용자 정의 예외
    @ExceptionHandler(value = PersonNotFoundException.class)    // 사용자가 존재하지 않을 경우 예외 발생
    public ResponseEntity<ExceptionResponseDto> handlePersonNotFoundException(PersonNotFoundException ex) {
        return new ResponseEntity<>(ExceptionResponseDto.of(HttpStatus.BAD_REQUEST.value(), ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
//    public handleArgumentException(IllegalArgumentException e) {
//
//    }
}
