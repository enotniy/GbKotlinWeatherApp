package com.gb.lesson2.ui.main.model

import com.gb.lesson2.BuildConfig
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header

const val API_KEY = "46fe3a8c-5ac7-4650-8578-40ab8b2a1082"

class RemoteDataSource {

    private val weatherApi = Retrofit.Builder()
        .baseUrl("https://api.weather.yandex.ru/")
        .addConverterFactory(
            GsonConverterFactory.create(GsonBuilder().setLenient().create())
        )
        .client(
            OkHttpClient.Builder().addInterceptor(
                HttpLoggingInterceptor().setLevel(
                    if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BODY
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                )
            ).build()
        )
        .build()
        .create(WeatherAPI::class.java)

    fun getWeatherDetail(lat: Double, lon: Double, callback: Callback<WeatherDTO>) {
        weatherApi.getWeather(API_KEY, lat, lon).enqueue(callback)
    }
}