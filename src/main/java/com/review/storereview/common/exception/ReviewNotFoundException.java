package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;

// 리뷰 게시글 조회 불가 에러
public class ReviewNotFoundException extends ReviewServiceException {
    public ReviewNotFoundException() {
        super(ApiStatusCode.REVIEW_NOT_FOUND);
    }

}
