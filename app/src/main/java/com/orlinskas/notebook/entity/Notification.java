package com.orlinskas.notebook.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.Objects;

import static com.orlinskas.notebook.Constants.*;

@Entity
@Parcel(Parcel.Serialization.BEAN)
public class Notification implements Comparable<Notification> {

    @SerializedName(NOTIFICATION_ID_API_NAME)
    @PrimaryKey
    private int id;

    @SerializedName(NOTIFICATION_CREATE_DATE_API_NAME)
    @ColumnInfo(name = "create_date_millis")
    private long createDateMillis;

    @SerializedName(NOTIFICATION_START_DATE_API_NAME)
    @ColumnInfo(name = "start_date_millis")
    private long startDateMillis;

    @SerializedName(NOTIFICATION_BODY_TEXT_API_NAME)
    @ColumnInfo(name = "body_text")
    private String bodyText;

    @SerializedName(NOTIFICATION_IS_SYNCHRONIZED_API_NAME)
    @ColumnInfo(name = "is_synchronized")
    private boolean isSynchronized;

    @SerializedName(NOTIFICATION_DELETED_AT_API_NAME)
    @ColumnInfo(name = "deleted_at")
    private String deleted_at;

    @Ignore
    public Notification() {
        //constructor to Mockito
    }

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
        return id == that.id &&
                createDateMillis == that.createDateMillis &&
                startDateMillis == that.startDateMillis;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createDateMillis, startDateMillis);
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
