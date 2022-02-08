package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;

public class ContentNotFound extends ReviewServiceException{
    public ContentNotFound() {
        super(ApiStatusCode.CONTENT_NOT_FOUND);
    }
}
