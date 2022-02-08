package com.review.storereview.common.exception;

import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;

import java.util.Map;

/**
 * api 요청 시 전달하는 파라미터에 문제 발생 시 호출되는 Exception
 * error_code : [400,"ParameterCheckFailed","문법상 또는 파라미터 오류가 있어서 서버가 요청사항을 처리하지 못함."]
 */
public class ParamValidationException extends ReviewServiceException{
    // 아마 삭제 예정
    public ParamValidationException() {
        super(ApiStatusCode.PARAMETER_CHECK_FAILED);
    }

    public ParamValidationException(Map<String, String> parameterErrorMsg) {
        super();
        super.errorStatusCode = ApiStatusCode.PARAMETER_CHECK_FAILED;
        System.out.println("ParamValidationException.ParamValidationException 호출됨");
        super.responseJsonObject = ResponseJsonObject.withParameterMsg(
                errorStatusCode.getCode(), errorStatusCode.getType(), errorStatusCode.getMessage(), parameterErrorMsg
        );
    }
}
