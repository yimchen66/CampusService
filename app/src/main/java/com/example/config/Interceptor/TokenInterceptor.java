package com.example.config.Interceptor;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.MainActivity;
import com.example.utils.SharedPreferencesUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {
    private static final String HEADER_TOKEN = "token";
    private Context context;

    public TokenInterceptor(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        //获取token
        String token = SharedPreferencesUtil.getString(context, "token");

        if(token == null)
            token = "";

        Request originalRequest = chain.request();
        Request.Builder builder = originalRequest.newBuilder()
                .header(HEADER_TOKEN, token);
        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}
