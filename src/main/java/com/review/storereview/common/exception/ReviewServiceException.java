package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;

public class ReviewServiceException extends RuntimeException{
    protected ApiStatusCode errorStatusCode ;
    protected ResponseJsonObject responseJsonObject;

    public ResponseJsonObject getResponseJsonObject(){
        return responseJsonObject;
    }

    public ReviewServiceException() {
    }


    public ReviewServiceException(ApiStatusCode errorStatusCode) {
        this.errorStatusCode = errorStatusCode;
        responseJsonObject = ResponseJsonObject.withError(errorStatusCode.getCode(), errorStatusCode.getType(), errorStatusCode.getMessage());
    }

}
