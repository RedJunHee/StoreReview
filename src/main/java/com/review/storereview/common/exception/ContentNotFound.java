package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;

public class ContentNotFound extends RuntimeException{
    private final ApiStatusCode errorStatusCode = ApiStatusCode.CONTENT_NOT_FOUND;
    private ResponseJsonObject responseJsonObject;

    public ResponseJsonObject getResponseJsonObject () {return responseJsonObject;}
    public ContentNotFound() {
        super();
        responseJsonObject = ResponseJsonObject.withStatusCode(errorStatusCode.getCode());}
}
