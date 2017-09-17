package com.example.manvi.movieappstage1.data.Source.remote;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieApi {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";


    private static Retrofit sRetrofit = null;

    public static Retrofit getClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        if (sRetrofit == null) {
            sRetrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .baseUrl(BASE_URL)
                    .client(builder.build())
                    .build();
        }
        return sRetrofit;
    }
}
