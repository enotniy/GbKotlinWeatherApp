package com.gb.lesson2.ui.main.model

import retrofit2.Callback


interface DetailsRepository {
    fun getWeatherDetailFromServer(lat: Double, lon: Double, callback: retrofit2.Callback<WeatherDTO>)
}