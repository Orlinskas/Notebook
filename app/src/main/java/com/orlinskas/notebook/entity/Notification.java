package com.orlinskas.notebook.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Objects;

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

    @ColumnInfo(name = "is_synchronized")
    private boolean isSynchronized;

    @ColumnInfo(name = "deleted_at")
    private String deleted_at;

    @ParcelConstructor
    public Notification(int id, long createDateMillis, long startDateMillis, String bodyText) {
        this.id = id;
        this.createDateMillis = createDateMillis;
        this.startDateMillis = startDateMillis;
        this.bodyText = bodyText;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Notification that = (Notification) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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

    public boolean isSynchronized() {
        return isSynchronized;
    }

    public String getDeleted_at() {
        return deleted_at;
    }

    public void setSynchronized(boolean aSynchronized) {
        isSynchronized = aSynchronized;
    }

    public void setDeleted_at(String deleted_at) {
        this.deleted_at = deleted_at;
    }
}
