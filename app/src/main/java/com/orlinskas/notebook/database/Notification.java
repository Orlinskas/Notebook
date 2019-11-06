package com.orlinskas.notebook.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Entity
@Parcel(Parcel.Serialization.BEAN)
public class Notification {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "create_date_millis")
    private long createDateMillis;

    @ColumnInfo(name = "start_date_millis")
    private long startDateMillis;

    @ColumnInfo(name = "start_date_full")
    private String startDateFull;

    @ColumnInfo(name = "start_day_date")
    private String startDayDate;

    @ColumnInfo(name = "start_day_time")
    private String startDayTime;

    @ColumnInfo(name = "start_day_name")
    private String startDayName;

    @ColumnInfo(name = "body_text")
    private String bodyText;

    @ParcelConstructor
    public Notification(long createDateMillis, long startDateMillis, String startDateFull,
                        String startDayDate, String startDayTime, String startDayName,
                        String bodyText) {
        this.createDateMillis = createDateMillis;
        this.startDateMillis = startDateMillis;
        this.startDateFull = startDateFull;
        this.startDayDate = startDayDate;
        this.startDayTime = startDayTime;
        this.startDayName = startDayName;
        this.bodyText = bodyText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public long getCreateDateMillis() {
        return createDateMillis;
    }

    public long getStartDateMillis() {
        return startDateMillis;
    }

    public String getStartDateFull() {
        return startDateFull;
    }

    public String getStartDayDate() {
        return startDayDate;
    }

    public String getStartDayTime() {
        return startDayTime;
    }

    public String getStartDayName() {
        return startDayName;
    }

    public String getBodyText() {
        return bodyText;
    }
}
