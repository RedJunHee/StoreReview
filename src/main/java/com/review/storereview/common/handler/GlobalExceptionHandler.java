package com.review.storereview.common.handler;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * controller 전역적인 예외처리 : 에러를 한 곳에서 처리
 *
 */
@RestControllerAdvice   // @ControllerAdvice + json형식의 파싱이 가능
public class GlobalExceptionHandler {
// 발생 위치를 정확하게 파악하기 위해  사용자 정의 예외를 사용
//    @ExceptionHandler(ExpectedException.class)
//    public handleArgumentException(IllegalArgumentException e) {
//
//    }
    // 에러 반환 객체 생성 필요
    // enum + 반환객체 세트로 감싸기.
}
