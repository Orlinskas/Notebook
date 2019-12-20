package com.orlinskas.notebook.service;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class Client {

    public static OkHttpClient create() {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

        httpClient.addInterceptor(chain -> {
            Request request = chain.request().newBuilder().addHeader("User-Id", "+380978946065").build();
            return chain.proceed(request);
        });

        return httpClient.build();
    }

}
