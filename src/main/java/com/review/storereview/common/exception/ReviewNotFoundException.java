package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;

// 리뷰 게시글 조회 불가 에러
public class ReviewNotFoundException extends RuntimeException {
    private final ApiStatusCode errorStatusCode = ApiStatusCode.REVIEW_NOT_FOUND;
    private final ResponseJsonObject responseJsonObject;

    public ResponseJsonObject getResponseJsonObject(){
        return responseJsonObject;
    }

    public ReviewNotFoundException() {
        responseJsonObject = ResponseJsonObject.withError(errorStatusCode.getCode(), errorStatusCode.getType(), errorStatusCode.getMessage());
    }

}
