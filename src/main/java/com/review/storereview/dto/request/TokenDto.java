package com.review.storereview.dto.request;

import lombok.*;


/**
 * Class       : TokenDto
 * Author      : 조 준 희
 * Description : JWT토큰 발행 ResponseDTO
 * History     : [2022-01-10] - 조 준희 - Class Create
 */
@Getter
public class TokenDto {

    private String token;

    public TokenDto(String token) {
        this.token = token;
    }
}