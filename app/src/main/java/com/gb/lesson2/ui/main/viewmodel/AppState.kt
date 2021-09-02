package com.gb.lesson2.ui.main.viewmodel

import com.gb.lesson2.ui.main.model.Weather

sealed class AppState {
    data class Success(val weather: Weather) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}