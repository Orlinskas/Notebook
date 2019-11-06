package com.orlinskas.notebook.date;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateCurrent {
    public static String getLine(String format) {
        SimpleDateFormat commonFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return commonFormat.format(new Date());
    }

    public static Date get() {
        return new Date();
    }
}
