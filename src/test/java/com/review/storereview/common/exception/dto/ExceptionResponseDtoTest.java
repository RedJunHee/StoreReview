package com.review.storereview.common.exception.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.review.storereview.common.enumerate.ApiStatusCode;
import org.junit.jupiter.api.Test;

class ExceptionResponseDtoTest {

    @Test
    void 빌드패턴_테스트()
    {
        ExceptionResponseDto dto = new ExceptionResponseDto(ApiStatusCode.PARAMETER_CHECK_FAILED);
        ExceptionResponseDto dto2 = new ExceptionResponseDto(ApiStatusCode.PARAMETER_CHECK_FAILED,"email is null.");


        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            System.out.println("dto : " + om.writeValueAsString(dto));
            System.out.println("dto2 : " + om.writeValueAsString(dto2));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

}