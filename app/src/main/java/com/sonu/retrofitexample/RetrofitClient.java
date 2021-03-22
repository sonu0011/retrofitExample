package com.sonu.retrofitexample;

import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String TAG = "MainActivity";
    private static RetrofitClient client;
    private static Retrofit retrofit;

    private RetrofitClient() {
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        OkHttpClient.Builder okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newReqeust = request
                                .newBuilder()
                                .addHeader("sonu", "kumar")
                                .build();

                        return chain.proceed(newReqeust);
                    }
                });


        if (BuildConfig.DEBUG) {
            okHttpClient.addInterceptor(loggingInterceptor);
        }
        retrofit = new Retrofit.Builder()
//                .baseUrl("https://jsonplaceholder.typicode.com/")
                .baseUrl("https://api.github.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient.build())
                .build();
    }

    public static synchronized RetrofitClient getInstance() {
        if (client == null) {
            return new RetrofitClient();
        }
        return client;
    }

    public APICalls getApi() {
        Log.d(TAG, "getApi: " + this.retrofit);
        return retrofit.create(APICalls.class);
    }

}
