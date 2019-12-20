package com.orlinskas.notebook.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.orlinskas.notebook.Constants.USER_ID;

public class Client {
    private static String userID;

    public Client() {
        userID = USER_ID;
    }

    public Client(String userID) {
        Client.userID = userID;
    }

    public static OkHttpClient create() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("User-Id", userID).build();
            return chain.proceed(request);
        });

        return httpClient.build();
    }

}
