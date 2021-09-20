package com.gb.lesson2.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.lesson2.ui.main.model.*
import com.gb.lesson2.ui.main.model.database.HistoryEntity
import com.gb.lesson2.ui.main.view.App
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.text.ParseException
import java.util.*

const val MAIN_LINK = "https://api.weather.yandex.ru/v2/informers?"

class DetailViewModel : ViewModel() {

    private val repository: DetailsRepository = DetailsRepositoryImpl(RemoteDataSource())
    private val localRepository: LocalRepository = LocalRepositoryImpl(App.getHistoryDao())
    private val detailLiveData = MutableLiveData<AppState>()

    val liveData: LiveData<AppState> = detailLiveData

    fun getWeatherFromRemoteSource(weather: Weather) {
        detailLiveData.value = AppState.Loading

        repository.getWeatherDetailFromServer(
            weather.city.lat,
            weather.city.lon,
            object : Callback<WeatherDTO> {

                override fun onFailure(call: Call<WeatherDTO>, t: Throwable) {
                    detailLiveData.postValue(AppState.Error(t))
                }

                override fun onResponse(call: Call<WeatherDTO>, response: Response<WeatherDTO>) {
                    response.body()?.let {
                        detailLiveData.postValue(checkResponse(it, weather.city))
                    }
                }
            })
    }

    private fun checkResponse(response: WeatherDTO, city: City): AppState {
        val factDTO = response.fact

        return if (factDTO?.condition != null
            && factDTO.condition.isNotBlank()
            && factDTO.temp != null
            && factDTO.feels_like != null
        ) {
            AppState.Success(
                listOf(
                    Weather(
                        city = city,
                        temperature = factDTO.temp,
                        feelsLike = factDTO.feels_like,
                        condition = factDTO.condition
                    )
                )
            )
        } else {
            AppState.Error(ParseException("Не смог распарсить json!", 0))
        }
    }

    fun saveWeather(weather: Weather) {
        localRepository.saveEntity(
            HistoryEntity(
                id = 0,
                city = weather.city.name,
                temperature = weather.temperature,
                condition = weather.condition,
                timestamp = Date().time
            )
        )
    }
}