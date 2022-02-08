package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;

public class PersonIdNotFoundException extends ReviewServiceException{
    public PersonIdNotFoundException() {
        super(ApiStatusCode.PERSONID_NOT_FOUND);
    }
}
