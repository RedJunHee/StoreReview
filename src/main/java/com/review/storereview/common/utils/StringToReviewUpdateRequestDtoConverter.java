package com.review.storereview.common.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.review.storereview.dto.request.ReviewUpdateRequestDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToReviewUpdateRequestDtoConverter implements Converter<String, ReviewUpdateRequestDto> {

    public ObjectMapper objectMapper;
    ReviewUpdateRequestDto updateRequestDto;

    public StringToReviewUpdateRequestDtoConverter(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public ReviewUpdateRequestDto convert(String source) {
        try {
            updateRequestDto = objectMapper.readValue(source, new TypeReference<ReviewUpdateRequestDto>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return updateRequestDto;
    }
}
