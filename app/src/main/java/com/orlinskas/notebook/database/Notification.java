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
    private String start_day_date;

    @ColumnInfo(name = "start_day_time")
    private String start_day_time;

    @ColumnInfo(name = "start_day_name")
    private String start_day_name;

    @ColumnInfo(name = "body_text")
    private String bodyText;

    @ParcelConstructor
    public Notification(int id, long createDateMillis, long startDateMillis, String startDateFull,
                        String start_day_date, String start_day_time, String start_day_name,
                        String bodyText) {
        this.id = id;
        this.createDateMillis = createDateMillis;
        this.startDateMillis = startDateMillis;
        this.startDateFull = startDateFull;
        this.start_day_date = start_day_date;
        this.start_day_time = start_day_time;
        this.start_day_name = start_day_name;
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

    public String getStart_day_date() {
        return start_day_date;
    }

    public String getStart_day_time() {
        return start_day_time;
    }

    public String getStart_day_name() {
        return start_day_name;
    }

    public String getBodyText() {
        return bodyText;
    }
}
