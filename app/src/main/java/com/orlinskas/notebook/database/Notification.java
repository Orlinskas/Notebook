package com.orlinskas.notebook.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Notification {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "create_date_millis")
    private long createDateMillis;

    @ColumnInfo(name = "start_date_millis")
    private long startDateMillis;

    @ColumnInfo(name = "start_date")
    private String startDate;

    @ColumnInfo(name = "body_text")
    private String bodyText;

    public Notification(long createDateMillis, long startDateMillis, String startDate, String bodyText) {
        this.createDateMillis = createDateMillis;
        this.startDateMillis = startDateMillis;
        this.bodyText = bodyText;
        this.startDate = startDate;
    }

    public long getCreateDateMillis() {
        return createDateMillis;
    }

    public long getStartDateMillis() {
        return startDateMillis;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getBodyText() {
        return bodyText;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
