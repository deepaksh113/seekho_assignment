package com.example.seekho_assignment.network

import com.example.seekho_assignment.utils.Constants
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    val instance: Retrofit by lazy {
        val okHttpClient = OkHttpClient.Builder()
            .readTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS)
            .connectTimeout(Constants.TIME_OUT, TimeUnit.MILLISECONDS)
            .addInterceptor{ chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                chain.proceed(requestBuilder.build())
            }

        val builder =
            Retrofit.Builder().baseUrl(Constants.BASE_URL).client(okHttpClient.build())
                .addConverterFactory(GsonConverterFactory.create())

        builder.build()
    }
}
