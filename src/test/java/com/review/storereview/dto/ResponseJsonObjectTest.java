package com.review.storereview.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.review.storereview.common.enumerate.ApiStatusCode;
import org.junit.jupiter.api.Test;

class ResponseJsonObjectTest {

    @Test
    void 빌드패턴_테스트()
    {
        ResponseJsonObject dto = ResponseJsonObject.withStatusCode(ApiStatusCode.PARAMETER_CHECK_FAILED);
        ResponseJsonObject dto2 = ResponseJsonObject.withError(ApiStatusCode.PARAMETER_CHECK_FAILED, ApiStatusCode.PARAMETER_CHECK_FAILED.getType(), ApiStatusCode.PARAMETER_CHECK_FAILED.getMessage());


        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            System.out.println("dto : " + om.writeValueAsString(dto));
            System.out.println("dto2 : " + om.writeValueAsString(dto2));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

}