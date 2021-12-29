package com.review.storereview.web.rest.controller.dto;

import com.review.storereview.dto.BaseResponseDto;
import org.junit.jupiter.api.Test;

class CommonResDtoTest {

    @Test
    public void CommonResDto테스트()
    {
        BaseResponseDto resDto = BaseResponseDto.builder()
                .withMeta(BaseResponseDto.Meta.builder()
                        .withCode(200)
                        .withMsg("MESSAGE TEMP....")
                        .build()
                )
                .build();

        System.out.println(resDto.toString());
    }
}