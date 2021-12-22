package com.review.StoreReview.web.rest.controller.dto;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommonResDtoTest {

    @Test
    public void CommonResDto테스트()
    {
        CommonResDto resDto = CommonResDto.builder()
                .withMeta(CommonResDto.Meta.builder()
                        .withCode(200)
                        .withMsg("MESSAGE TEMP....")
                        .build()
                )
                .build();

        System.out.println(resDto.toString());
    }
}