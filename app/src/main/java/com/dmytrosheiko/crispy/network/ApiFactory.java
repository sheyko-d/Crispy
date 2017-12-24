package com.dmytrosheiko.crispy.network;

import android.support.annotation.NonNull;

import com.dmytrosheiko.crispy.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Init retrofit interface and provide api services
 */
public class ApiFactory {

    private static final int CONNECT_TIMEOUT = 15;
    private static final int WRITE_TIMEOUT = 60;
    private static final int TIMEOUT = 60;

    private static final OkHttpClient CLIENT;
    private static final HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor
            (new SystemLogger("API"));
    private static Retrofit sRetrofit;

    static {
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        CLIENT = new OkHttpClient.Builder()
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                .addNetworkInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder().addHeader("Authorization", "Client-ID 21d0b65dda1bfb82854982e965c0300b27ca6bfd8ae0ded34e1b53875e74ec22").build();
                        return chain.proceed(request);
                    }
                })
                .build();

    }

    /**
     * Get api service
     *
     * @return api service
     */
    @NonNull
    public static ApiService getApiService() {
        return getRetrofit().create(ApiService.class);
    }


    @NonNull
    private static Retrofit getRetrofit() {
        if (sRetrofit == null) {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();
            sRetrofit = new Retrofit.Builder()
                    .baseUrl(Util.BASE_API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(CLIENT)
                    .build();
        }
        return sRetrofit;
    }


    private static class SystemLogger implements HttpLoggingInterceptor.Logger {
        private final String tag;

        SystemLogger(@NonNull String tag) {
            this.tag = tag;
        }

        @Override
        public void log(String message) {
            Util.splitOutput(tag, message);
        }
    }
}
