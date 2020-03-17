package com.udacity.asteroidradar.data.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.udacity.asteroidradar.utils.Constants
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitInstance {
    //    private val authInterceptor = Interceptor { chain ->
//        val newUrl = chain.request().url
//                .newBuilder()
//                .addQueryParameter("api_key", Constants.API_KEY)
//                .build()
//
//        val newRequest = chain.request()
//                .newBuilder()
//                .url(newUrl)
//                .build()
//
//
//        chain.proceed(newRequest)
//    }
    val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

//    private val appClient = OkHttpClient().newBuilder()
//            .addInterceptor(authInterceptor)
//            .build()


    var logging = HttpLoggingInterceptor()
    var client = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .addInterceptor(logging.setLevel(HttpLoggingInterceptor.Level.BODY))
            .build()

    fun retrofitWithScalar(): Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            // .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    fun retrofitWithMoshi(): Retrofit = Retrofit.Builder()
            .client(client)
            .baseUrl(Constants.BASE_URL)
            //.addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

    val appAPIWithScalar: API = retrofitWithScalar().create(API::class.java)

    val appAPIWithMoshi: API = retrofitWithMoshi().create(API::class.java)


}
