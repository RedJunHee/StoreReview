package com.review.storereview.common.exception.dto;

import lombok.AllArgsConstructor;
// 에러 반환 dto
@AllArgsConstructor(staticName = "of")  // 생성자를 감싸는 정적팩토리 메서드
public class ExceptionResponseDto {
    private int code;
    private String message;
}
