package com.review.storereview.web.rest.controller.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.storereview.common.enumerate.ApiStatusCode;
import com.review.storereview.dto.ResponseJsonObject;
import com.review.storereview.dto.response.BaseResponseDto;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


class CommonResDtoTest {


    class TestClass implements BaseResponseDto {
        String id = "testId";
        int seq = 12313;
        String name = "조준희";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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
    public void CommonResDto테스트() throws JsonProcessingException {

        List<TestClass> listTest = new ArrayList<>();
        listTest.add(new TestClass());
        listTest.add(new TestClass());
        listTest.add(new TestClass());
        listTest.add(new TestClass());
        listTest.add(new TestClass());

        ResponseJsonObject resDto = ResponseJsonObject.builder()
                .withMeta( ResponseJsonObject.Meta.builder().withCode(ApiStatusCode.OK).build() )
                .withData(new TestClass())
                .build();

        ResponseJsonObject resListDto = ResponseJsonObject.builder()
                .withMeta( ResponseJsonObject.Meta.builder().withCode(ApiStatusCode.OK).build() )
                .withData(listTest)
                .build();


        ObjectMapper om = new ObjectMapper();
        String str = om.writeValueAsString(resDto);
        String strList = om.writeValueAsString(resListDto);
        System.out.println(str);
        System.out.println(strList);
    }
}