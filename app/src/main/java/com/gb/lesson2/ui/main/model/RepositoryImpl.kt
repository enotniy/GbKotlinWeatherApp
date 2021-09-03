package com.gb.lesson2.ui.main.model

class RepositoryImpl: Repository {
    override fun getWeatherFromServer(): Weather = Weather()
    override fun getWeatherFromLocalStorageRus(): List<Weather> = getRussianCities()
    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()
}