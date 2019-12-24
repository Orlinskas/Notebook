package com.orlinskas.notebook.service;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;

import static com.orlinskas.notebook.Constants.DEFAULT_USER_ID;
import static com.orlinskas.notebook.Constants.OK_HTTP_TIMEOUT;

public class Client {
    private static String userID = DEFAULT_USER_ID;

    public Client(String userID) {
        Client.userID = userID;
    }

    public static OkHttpClient create() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.connectTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS);
        httpClient.readTimeout(OK_HTTP_TIMEOUT, TimeUnit.SECONDS);

        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("User-Id", userID).build();
            return chain.proceed(request);
        });

        return httpClient.build();
    }
}
