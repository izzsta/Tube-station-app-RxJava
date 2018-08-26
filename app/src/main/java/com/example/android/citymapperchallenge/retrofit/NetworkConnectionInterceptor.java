package com.example.android.citymapperchallenge.retrofit;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by izzystannett on 29/04/2018.
 */

public abstract class NetworkConnectionInterceptor implements Interceptor {

    //interceptor for internet connectivity check
    public abstract boolean isInternetAvailable();

    public abstract void onInternetUnavailable();

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!isInternetAvailable()) {
            onInternetUnavailable();
        }
        return chain.proceed(request);
    }
}
