package com.review.storereview.dto.response;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;

class UserResponseDtoTest {

    class TestClass implements BaseResponseDto {
        String userId = "testId";
        int seq = 12313;
        String name = "조준희";

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public int getSeq() {
            return seq;
        }

        public void setSeq(int seq) {
            this.seq = seq;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    @Test
    public void UserResponseDto테스트() throws JsonProcessingException {

        List<UserResponseDtoTest.TestClass> listTest = new ArrayList<>();
        listTest.add(new UserResponseDtoTest.TestClass());
        listTest.add(new UserResponseDtoTest.TestClass());
        listTest.add(new UserResponseDtoTest.TestClass());
        listTest.add(new UserResponseDtoTest.TestClass());
        listTest.add(new UserResponseDtoTest.TestClass());

        ResponseJsonObject resDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()).setData(new UserResponseDtoTest.TestClass());

        ResponseJsonObject resListDto = ResponseJsonObject.withStatusCode(ApiStatusCode.OK.getCode()).setData(listTest);

        ObjectMapper om = new ObjectMapper();
        String str = om.writeValueAsString(resDto);
        String strList = om.writeValueAsString(resListDto);
        System.out.println(str);
        System.out.println(strList);
    }
}