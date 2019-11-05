package com.orlinskas.notebook.date;

import java.util.Calendar;
import java.util.Date;

public class DateCalculator {

    public String plusDays(String dateLine, int countDays, String dateFormat) {
        Date date = DateHelper.parse(dateLine, dateFormat);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.DATE, countDays);
        Date dateAfterPlusDay = c.getTime();

        return DateHelper.parse(dateAfterPlusDay, dateFormat);
    }

    public String plusHours(String dateLine, int countHours, String dateFormat) {
        Date date = DateHelper.parse(dateLine, dateFormat);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.add(Calendar.HOUR_OF_DAY, countHours);
        Date dateAfterPlusDay = c.getTime();

        return DateHelper.parse(dateAfterPlusDay, dateFormat);
    }

    public int calculateDifferencesInHours(Date old, Date now) {
        long milliseconds = now.getTime() - old.getTime();
        return (int) (milliseconds / (60 * 60 * 1000));
    }
}