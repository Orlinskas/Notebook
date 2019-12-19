package com.orlinskas.notebook.database;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.date.DateCalculator;
import com.orlinskas.notebook.date.DateCurrent;
import com.orlinskas.notebook.date.DateFormater;
import com.orlinskas.notebook.entity.Notification;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Random;

import static junit.framework.TestCase.assertTrue;
import static junit.framework.TestCase.fail;

@RunWith(MockitoJUnitRunner.class)
public class NotificationDaoTest {
    private MyDatabase database = App.getInstance().getMyDatabase();
    private Context context = InstrumentationRegistry.getInstrumentation().getContext();
    private Notification mockNotification = Mockito.mock(Notification.class);

    @Before
    public void setUp() {
        String currentDate = DateCurrent.getLine(DateFormater.YYYY_MM_DD_HH_MM);
        String datePlusHour = new DateCalculator().plusHours(currentDate, 1, DateFormater.YYYY_MM_DD_HH_MM);
        //val notification = NotificationBuilder(context).build("Test", datePlusHour)
        Mockito.when(mockNotification .getId()).thenReturn(new Random().nextInt(2000000000 - 10 + 1) + 10);
        Mockito.when(mockNotification .getBodyText()).thenReturn("Test");
        Mockito.when(mockNotification .getCreateDateMillis()).thenReturn(System.currentTimeMillis() + (60 * 60 * 1000));
        Mockito.when(mockNotification .getStartDateMillis()).thenReturn(System.currentTimeMillis() + 2 *(60 * 60 * 1000));
        Mockito.when(mockNotification .getDeleted_at()).thenReturn(null);
        Mockito.when(mockNotification .isSynchronized()).thenReturn(true);
    }

    @Test
    public void findAll() {
        boolean isContain = false;
        database.notificationDao().insertAll(mockNotification);
        ArrayList<Notification> notifications = (ArrayList<Notification>) database.notificationDao().findAll();

        for (Notification notification : notifications) {
            if (notification.getId() == mockNotification.getId()) {
                isContain = true;
                break;
            }
        }
        assertTrue(isContain);
    }

    @Test
    public void findActual() {

    }

    @Test
    public void insertAll() {

    }

    @Test
    public void delete() {
        for(Notification notification : database.notificationDao().findAll()) {
            if(notification.getBodyText().equals(mockNotification.getBodyText())) {
                database.notificationDao().delete(mockNotification);
            }
        }

        for (Notification notification : database.notificationDao().findAll()) {
            if (notification.getBodyText().equals("Test")) {
                fail();
            }
        }

        assertTrue(true);
    }

    @Test
    public void update() {

    }
}