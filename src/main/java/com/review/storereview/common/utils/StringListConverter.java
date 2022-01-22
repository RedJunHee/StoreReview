package com.review.storereview.common.utils;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.List;

public class StringListConverter implements AttributeConverter<List<String>, String> {
    private static final String SPLIT_CHAR = ",";

    // List<String>을 String으로
    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        return String.join(SPLIT_CHAR, stringList);
    }

    // String을 List로
    @Override
    public List<String> convertToEntityAttribute(String string) {
        return Arrays.asList(string.split(SPLIT_CHAR));
    }
}
