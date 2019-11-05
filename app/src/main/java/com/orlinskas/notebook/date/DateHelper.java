package com.orlinskas.notebook.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateHelper {
    public static String getCurrent(String format) {
        SimpleDateFormat commonFormat = new SimpleDateFormat(format, Locale.ENGLISH);
        return commonFormat.format(new Date());
    }

    public static Date getCurrentDate(String format) {
        try {
            return new SimpleDateFormat(format, Locale.ENGLISH).parse(DateHelper.getCurrent(format));
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }

    public static String parse(Date date, String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
    }

    public static Date parse(String date, String format) {
        try {
            return new SimpleDateFormat(format, Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}
