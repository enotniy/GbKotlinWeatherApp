package com.gb.lesson2.ui.main.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.gb.lesson2.ui.main.model.Repository
import com.gb.lesson2.ui.main.model.RepositoryImpl
import kotlin.random.Random

class MainViewModel : ViewModel() {

    private val liveDataToObserve: MutableLiveData<AppState> = MutableLiveData()
    private val repository: Repository = RepositoryImpl()

    val liveData: LiveData<AppState> = liveDataToObserve

    fun getWeatherFromLocalSource() = getDataFromLocalSource()
    fun getWeatherFromRemoteSource() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        liveDataToObserve.value = AppState.Loading

        Thread {
            Thread.sleep(5000)
            if (Random.nextBoolean()) {
                liveDataToObserve.postValue(AppState.Success(repository.getWeatherFromLocalStorage()))
            } else {
                liveDataToObserve.postValue(AppState.Error(Exception("Нет интернета")))
            }
        }.start()
    }
}