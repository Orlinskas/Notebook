package com.orlinskas.notebook.date;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateFormater {
    public final static String YYYY_MM_DD_HH_00 = "yyyy-MM-dd HH:00";
    public final static String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public final static String HH_MM = "HH:mm";
    public final static String YYYY_MM_DD = "yyyy-MM-dd";
    public final static String EEEE = "EEEE";

    public static String format(Date date, String format) {
        return new SimpleDateFormat(format, Locale.ENGLISH).format(date);
    }

    public static Date format(String date, String format) {
        try {
            return new SimpleDateFormat(format, Locale.ENGLISH).parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return new Date();
        }
    }
}