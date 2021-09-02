package com.gb.lesson2.ui.main.model

data class Weather(
    val city: City = getDefaultCity(),
    val temperature: Int = 0,
    val feelsLike: Int = 0
)

fun getDefaultCity(): City = City("Москва", 55.755826, 37.617299900000035)