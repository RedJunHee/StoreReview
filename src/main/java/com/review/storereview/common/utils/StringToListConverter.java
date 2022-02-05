package com.review.storereview.common.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.storereview.dto.request.ReviewUploadRequestDto;
import lombok.SneakyThrows;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
/**
 * Class       : StringToListConverter
 * Author      : 문 윤 지
 * Description : form-data로 온 input 에서 json형태의 String을 ReviewUploadRequestDto로 변환하는 Converter
 * History     : [2022-02-05] - 문 윤 지 - Class Create
 */
@Component // MVC 웹 설정을 따로 할 필요 x
public class StringToListConverter implements Converter<String, ReviewUploadRequestDto> {

    private ObjectMapper objectMapper;

    public StringToListConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @SneakyThrows
    @Override
    public ReviewUploadRequestDto convert(String source) {
        return objectMapper.readValue(source, new TypeReference<ReviewUploadRequestDto>() {});
    }
}
