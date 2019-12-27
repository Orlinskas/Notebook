package com.orlinskas.notebook.service.response;

import com.google.gson.annotations.SerializedName;
import com.orlinskas.notebook.MVVM.model.Notification;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

@Parcel(Parcel.Serialization.BEAN)
public class ResponseDELETE {

    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Notification notification;

    @ParcelConstructor
    public ResponseDELETE(Integer code, String message, Notification notification) {
        this.code = code;
        this.message = message;
        this.notification = notification;
    }

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Notification getNotification() {
        return notification;
    }
}