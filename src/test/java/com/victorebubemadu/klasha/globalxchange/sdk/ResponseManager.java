package com.victorebubemadu.klasha.globalxchange.sdk;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.victorebubemadu.klasha.globalxchange.controller.Response;

import okhttp3.OkHttpClient;

public class ResponseManager {

    private static Gson gson = new Gson();

    private static OkHttpClient client = new OkHttpClient.Builder()
            .readTimeout(1, java.util.concurrent.TimeUnit.DAYS)
            .build();

    public <T> T data(okhttp3.Request httpRequest, TypeToken<T> token) {
        try (var httpResponse = client.newCall(httpRequest).execute()) {

            var resJson = httpResponse.body().string();

            T response = gson.fromJson(resJson, token.getType());

            return response;
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

}
