package com.procentplus.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static Retrofit ourInstance;

    public static Retrofit getInstance() {
        if (ourInstance == null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient.Builder client = new OkHttpClient.Builder()
                    .addInterceptor(interceptor);

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            ourInstance = new Retrofit.Builder()
                    .baseUrl("https://procentplus.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client.build())
                    .build();
        }
        return ourInstance;

    }

    private RetrofitClient() {

    }

}
