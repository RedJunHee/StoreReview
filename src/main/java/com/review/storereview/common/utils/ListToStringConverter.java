package com.review.storereview.common.utils;

import com.amazonaws.util.CollectionUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
@Converter
public class ListToStringConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ",";

    // List<String>을 String으로 변환 후 db저장
    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if (CollectionUtils.isNullOrEmpty(stringList)) // null 체크
            return null;
        return String.join(SPLIT_CHAR, stringList);
    }

    // String을 List로 변환 후 전달
    @Override
    public List<String> convertToEntityAttribute(String string) {
        if (string == null || string.isEmpty())    // null 체크
            return Collections.emptyList();
        return Arrays.asList(string.split(SPLIT_CHAR));
    }
}