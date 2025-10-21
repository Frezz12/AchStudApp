package com.example.achstudapp.api;

import android.util.Log;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.*;

public class ApiClient {
    private static Retrofit retrofit;

    public static Retrofit getClient(String token) {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                .addInterceptor(logging);

        if (token != null && !token.isEmpty()) {
            clientBuilder.addInterceptor(chain -> {
                Log.d("ApiClient", "Token: " + token);
                Request original = chain.request();

                Request request = original.newBuilder()
                        .addHeader("Content-Type", "application/json")
                        .addHeader("Authorization", "Bearer " + token)
                        .method(original.method(), original.body())
                        .build();
                return chain.proceed(request);
            });
        }

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl("http://158.160.206.19:3000/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(clientBuilder.build())
                    .build();
        }
        return retrofit;
    }
}