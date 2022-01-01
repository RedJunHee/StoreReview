package com.review.storereview.common.enumerate;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ApiStatusCodeTest {

    @Test
    void APISTATUSCODE_테스트() {
        Assertions.assertThat(ApiStatusCode.OK.getCode()).isEqualTo(200);
        Assertions.assertThat(ApiStatusCode.OK.getType()).isEqualTo("OK");
        Assertions.assertThat(ApiStatusCode.OK.getMessage()).isEqualTo("성공.");

        Assertions.assertThat(ApiStatusCode.PARAMETER_CHECK_FAILED.getCode()).isEqualTo(400);
        Assertions.assertThat(ApiStatusCode.PARAMETER_CHECK_FAILED.getType()).isEqualTo("ParameterCheckFailed");
        Assertions.assertThat(ApiStatusCode.PARAMETER_CHECK_FAILED.getMessage()).isEqualTo("문법상 또는 파라미터 오류가 있어서 서버가 요청사항을 처리하지 못함.");

        Assertions.assertThat(ApiStatusCode.SYSTEM_ERROR.getCode()).isEqualTo(599);
        Assertions.assertThat(ApiStatusCode.SYSTEM_ERROR.getType()).isEqualTo("SystemError");
        Assertions.assertThat(ApiStatusCode.SYSTEM_ERROR.getMessage()).isEqualTo("시스템오류.");

    }

}