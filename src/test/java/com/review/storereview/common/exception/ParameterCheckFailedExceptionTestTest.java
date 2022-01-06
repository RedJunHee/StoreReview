package com.review.storereview.common.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.*;

class ParameterCheckFailedExceptionTestTest {

    ObjectMapper om ;

    @BeforeEach
    void beforeEach() {
        om = new ObjectMapper().registerModule(new JavaTimeModule());

    }

    @Test
    @DisplayName("추가 메시지를 지닌 예외의 경우")
    void 파라미터에외_테스트 ()
    {
        ParameterCheckFailedExceptionTest ex =  Assertions.assertThrows(ParameterCheckFailedExceptionTest.class, ()
        ->{
            throw new ParameterCheckFailedExceptionTest("parameter email is null.");
        });

        try {
            System.out.println(om.writeValueAsString(ex.getExceptionResponseDto()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    @DisplayName("추가 메시지 없는 예외의 경우")
    void 파라미터에외_테스트2 ()
    {
        ParameterCheckFailedExceptionTest ex =  Assertions.assertThrows(ParameterCheckFailedExceptionTest.class, ()
                ->{
            throw new ParameterCheckFailedExceptionTest();
        });

        try {
            System.out.println(om.writeValueAsString(ex.getExceptionResponseDto()));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}