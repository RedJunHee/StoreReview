package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;

// 회원가입 시 이미 회원이 존재하는 경우 발생하는 에러
public class PersonAlreadyExistsException extends ReviewServiceException{
    public PersonAlreadyExistsException() {
        super(ApiStatusCode.PERSON_ALREADY_EXISTS);
    }
}
