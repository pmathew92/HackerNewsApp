package com.prince.hackernewsapp.network;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://hacker-news.firebaseio.com/v0/";
    private static Retrofit instance = null;

    private RetrofitClient() {
    }

    /**
     * Method to return a single retrofit instance
     *
     * @return
     */
    public static Retrofit getInstance() {
        if (instance == null) {
            instance = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }

        return instance;
    }

    public static ApiService getApiService() {
        return getInstance().create(ApiService.class);
    }
}
