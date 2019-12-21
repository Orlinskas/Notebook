package com.orlinskas.notebook.service.response;

import com.google.gson.annotations.SerializedName;
import com.orlinskas.notebook.entity.Notification;

public class ResponsePOST {

    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private Notification notification;

    public ResponsePOST(Integer code, String message, Notification notification) {
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

    public Notification getData() {
        return notification;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNotification(Notification notification) {
        this.notification = notification;
    }
}