package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;

// 회원 조회 불가 에러
public class PersonNotFoundException extends RuntimeException {

    private final ApiStatusCode errorStatusCode = ApiStatusCode.PERSON_NOT_FOUND;
    private final ResponseJsonObject responseJsonObject;

    public ResponseJsonObject getResponseJsonObject(){
        return responseJsonObject;
    }

    public PersonNotFoundException() {
        responseJsonObject = ResponseJsonObject.withStatusCode(errorStatusCode);
    }
}
