package com.review.storereview.common.exception.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.review.storereview.common.enumerate.ApiStatusCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ExceptionResponseDtoTestTest {

    @Test
    void 빌드패턴_테스트()
    {
        ExceptionResponseDtoTest dto = new ExceptionResponseDtoTest(ApiStatusCode.PARAMETER_CHECK_FAILED);
        ExceptionResponseDtoTest dto2 = new ExceptionResponseDtoTest(ApiStatusCode.PARAMETER_CHECK_FAILED,"email is null.");


        ObjectMapper om = new ObjectMapper().registerModule(new JavaTimeModule());
        try {
            System.out.println("dto : " + om.writeValueAsString(dto));
            System.out.println("dto2 : " + om.writeValueAsString(dto2));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

}