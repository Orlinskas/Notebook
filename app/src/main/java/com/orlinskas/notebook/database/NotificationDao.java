package com.orlinskas.notebook.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.orlinskas.notebook.entity.Notification;

import java.util.List;

@Dao
public interface NotificationDao {
    @Query("SELECT * FROM notification")
    List<Notification> findAll();

    @Query("SELECT * FROM notification WHERE start_date_millis > :currentDateMillis")
    List<Notification> findActual(long currentDateMillis);

    @Insert
    void insertAll(Notification... notifications);

    @Delete
    void delete(Notification notification);
}
