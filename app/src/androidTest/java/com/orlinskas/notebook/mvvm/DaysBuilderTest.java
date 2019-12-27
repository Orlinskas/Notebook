package com.orlinskas.notebook.mvvm;

import com.orlinskas.notebook.builder.DaysBuilder;
import com.orlinskas.notebook.date.DateCalculator;
import com.orlinskas.notebook.date.DateFormater;
import com.orlinskas.notebook.date.DateCurrent;
import com.orlinskas.notebook.mvvm.model.Day;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class DaysBuilderTest {
    private String currentDate;
    private DaysBuilder daysBuilder;

    @Before
    public void setUp() {
        currentDate = DateCurrent.getLine(DateFormater.YYYY_MM_DD);
        daysBuilder = new DaysBuilder();
    }

    @Test
    public void findActual() {
        List<Day> days = daysBuilder.findActual();
        String checkedDate = new DateCalculator().plusDays(currentDate, 6, DateFormater.YYYY_MM_DD);
        assertEquals(days.get(6).getDayDate(), checkedDate);
    }
}