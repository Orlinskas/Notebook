package com.orlinskas.notebook.service.response;

import com.google.gson.annotations.SerializedName;
import com.orlinskas.notebook.mVVM.model.Notification;

import org.parceler.Parcel;
import org.parceler.ParcelConstructor;

import java.util.List;

@Parcel(Parcel.Serialization.BEAN)
public class ResponseGET {

    @SerializedName("code")
    private Integer code;

    @SerializedName("message")
    private String message;

    @SerializedName("data")
    private List<Notification> data = null;

    public Integer getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<Notification> getData() {
        return data;
    }

    @ParcelConstructor
    public ResponseGET(Integer code, String message, List<Notification> data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}