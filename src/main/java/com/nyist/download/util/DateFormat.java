package com.nyist.download.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateFormat {

    public static String formatDate(Date date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String format = sdf.format(date);
        return format;
    }
}
