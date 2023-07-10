package com.szy.loginscreen;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.Request;

import com.apollographql.apollo.ApolloClient;
import com.apollographql.apollo.api.cache.http.HttpCachePolicy;

public class ApolloClientProvider {
    private static ApolloClient apolloClient;
    private static final String BASE_URL = "https://test-service.cerberuslink.net/graphql";
    private static final String AUTH_TOKEN = "b13f3a77edc38e87e7427608a70485fb";
    public static ApolloClient getApolloClient() {
        if (apolloClient == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(chain -> {
                        Request originalRequest = chain.request();
                        Request.Builder builder = originalRequest.newBuilder().method(originalRequest.method(), originalRequest.body());
                        builder.header("Authorization", AUTH_TOKEN);
                        return chain.proceed(builder.build());
                    })
                    .build();

            apolloClient = ApolloClient.builder()
                    .serverUrl(BASE_URL)
                    .okHttpClient(okHttpClient)
                    .defaultHttpCachePolicy(HttpCachePolicy.NETWORK_ONLY)
                    .build();
        }

        return apolloClient;
    }
}


