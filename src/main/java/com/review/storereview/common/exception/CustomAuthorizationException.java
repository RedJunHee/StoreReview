package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;

/**
 * Class       : AuthorizationException
 * Author      : 조 준 희
 * Description : Spring Security JWT 인증 과정에서 생기는 ExceptionHandler에서 사용되어지는 객체.
 * ExceptionHandler = com.review.storereview.common.exception.handler.AuthorizationExceptionHandler.java
 * History     : [2022-01-10] - 조 준희 - Class Create
 */
public class CustomAuthorizationException  {
    private final ApiStatusCode errorStatusCode = ApiStatusCode.FORBIDDEN;
    private final ResponseJsonObject responseJsonObject;

    public ResponseJsonObject getResponseJsonObject(){
        return responseJsonObject;
    }

    public CustomAuthorizationException() {

        responseJsonObject = ResponseJsonObject.withStatusCode(errorStatusCode.getCode());
    }

    public CustomAuthorizationException(String message) {
        responseJsonObject = ResponseJsonObject.withError(errorStatusCode.getCode(), errorStatusCode.getType(), errorStatusCode.getMessage());
    }

}
