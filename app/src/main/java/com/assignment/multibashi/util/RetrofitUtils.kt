package com.assignment.multibashi.util

import com.assignment.multibashi.data.remote.VideoDataResponseModel
import com.assignment.multibashi.data.remote.VideoIdsData
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Url

private var retrofit: Retrofit? = null

private const val BASE_URL = "https://www.getpostman.com/collections/"

fun getRetrofitInstance(): Retrofit = retrofit ?: with(Retrofit.Builder()) {
    baseUrl(BASE_URL)
    addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
    val interceptor = HttpLoggingInterceptor()
    interceptor.level = HttpLoggingInterceptor.Level.BODY
    val client = OkHttpClient.Builder()
        .addInterceptor(interceptor)
        .build()
    client(client)
    retrofit = build()
    retrofit!!
}

interface ApiService {

    @GET("94abd54c9aef430af768")
    fun getVideoData():Call<VideoDataResponseModel>

    @GET
    fun getVideoIds(@Url url:String):Call<List<VideoIdsData>>

}
