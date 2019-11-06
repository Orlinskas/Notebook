package com.orlinskas.notebook.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public static String getCurrent(String format) {
        SimpleDateFormat commonFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return commonFormat.format(new Date());
    }

    public static Date getCurrentDate() {
        return new Date();
    }
}
