package com.xmd.log;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DateUtil {
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 毫秒值转换成日期
     */
    public static String longToDate(Long date) {
        SimpleDateFormat format = new SimpleDateFormat(FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return format.format(calendar.getTime());
    }
}
