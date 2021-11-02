package com.koreait.board;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetroFitObj {
    public static Retrofit rfo;
    private RetroFitObj() { }

    public static Retrofit getInstance() {
        if (rfo == null) {
            rfo = new Retrofit.Builder()
                    .baseUrl("http://192.168.120.1:8089/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return rfo;
    }

}

