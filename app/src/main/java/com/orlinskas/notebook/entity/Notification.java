package com.orlinskas.notebook.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Entity
@Parcel(Parcel.Serialization.BEAN)
public class Notification implements Comparable<Notification>{
    @PrimaryKey
    private int id;

    @ColumnInfo(name = "create_date_millis")
    private long createDateMillis;

    @ColumnInfo(name = "start_date_millis")
    private long startDateMillis;

    @ColumnInfo(name = "body_text")
    private String bodyText;

    @ParcelConstructor
    public Notification(int id, long createDateMillis, long startDateMillis, String bodyText) {
        this.id = id;
        this.createDateMillis = createDateMillis;
        this.startDateMillis = startDateMillis;
        this.bodyText = bodyText;
    }

    @Override
    public int compareTo(Notification o) {
        int result = (int) (startDateMillis - o.startDateMillis);

        if(result != 0) {
            return result / Math.abs(result);
        }

        return result;
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

    public String getBodyText() {
        return bodyText;
    }
}
