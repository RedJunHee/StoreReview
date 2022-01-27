package com.review.storereview.common.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Class       : StringUtil
 * Author      : 조 준 희
 * Description : Class Description
 * History     : [2022-01-27] - 조 준희 - Class Create
 */
public class StringUtil {


    public static String DateTimeToString(LocalDateTime datetime)
    {
        if(datetime == null)
            return null;

        String returnStr = datetime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));

        return returnStr;

    }
}
