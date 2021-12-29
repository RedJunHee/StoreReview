package com.review.storereview.common;

import com.review.storereview.common.enumerate.ApiStatusCode;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ApiStatusCodeTest {

    @Test
    void APISTATUSCODE_테스트() {
        Assertions.assertThat(ApiStatusCode.OK.getKey()).isEqualTo(200);
        Assertions.assertThat(ApiStatusCode.OK.getType()).isEqualTo("OK");
        Assertions.assertThat(ApiStatusCode.OK.getMessage()).isEqualTo("성공.");

        Assertions.assertThat(ApiStatusCode.PARAMETER_CHECK_FAILED.getKey()).isEqualTo(400);
        Assertions.assertThat(ApiStatusCode.PARAMETER_CHECK_FAILED.getType()).isEqualTo("ParameterCheckFailed");
        Assertions.assertThat(ApiStatusCode.PARAMETER_CHECK_FAILED.getMessage()).isEqualTo("문법상 또는 파라미터 오류가 있어서 서버가 요청사항을 처리하지 못함.");

        Assertions.assertThat(ApiStatusCode.SYSTEM_ERROR.getKey()).isEqualTo(599);
        Assertions.assertThat(ApiStatusCode.SYSTEM_ERROR.getType()).isEqualTo("SystemError");
        Assertions.assertThat(ApiStatusCode.SYSTEM_ERROR.getMessage()).isEqualTo("시스템오류.");

    }

}