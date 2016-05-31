package com.paras.flickrsnap.retro;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import io.realm.RealmObject;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Copyright Text as per company
 */

/**
 * RetroServiceGenerator.java - Configures Retro and provides method to generate service class for
 * entities
 *
 * @author Paras Mendiratta
 * @version 1.0
 * @since 2016-05-30
 */
public class RetroServiceGenerator {

    private static OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

    protected static Retrofit.Builder builder = null;
    public static Retrofit retrofit;

    /**
     * Creates custom Parser for removing fields of RealmObject class
     *
     * @return parsed GSON
     */
    private static Gson customParser() {
        Gson gson = new GsonBuilder()
                .setExclusionStrategies(new ExclusionStrategy() {
                    @Override
                    public boolean shouldSkipField(FieldAttributes f) {
                        return f.getDeclaringClass().equals(RealmObject.class);
                    }

                    @Override
                    public boolean shouldSkipClass(Class<?> clazz) {
                        return false;
                    }
                })
                .create();
        return gson;
    }

    /**
     * Initializes Retrofit
     */
    public static void init(String baseURL, final String apiKey) {

        httpClient.addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {

                Request original = chain.request();
                HttpUrl originalHttpUrl = original.url();

                // Add Query param for Flickr
                HttpUrl url = originalHttpUrl.newBuilder()
                        .addQueryParameter("api_key", apiKey)
                        .addQueryParameter("format", "json")
                        .addQueryParameter("nojsoncallback", "1")
                        .build();

                // Request customization: add request headers
                Request.Builder requestBuilder = original.newBuilder()
                        .url(url);

                Request request = requestBuilder.build();
                return chain.proceed(request);
            }
        });


        OkHttpClient client = httpClient.build();

        RetroServiceGenerator.builder =
                new Retrofit.Builder()
                        .baseUrl(baseURL)
                        .client(client)
                        .addConverterFactory(GsonConverterFactory.create(customParser()));

        retrofit = builder.client(httpClient.build()).build();
    }

    /**
     * Create an implementation of the API endpoints defined by the {@code service} interface.
     *
     * @param serviceClass - class object of Service
     * @param <S>          - Type of class
     *
     * @return - Reference of generated service interface
     */
    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }
}
