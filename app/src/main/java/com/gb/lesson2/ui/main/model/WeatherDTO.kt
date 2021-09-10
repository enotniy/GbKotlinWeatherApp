package com.gb.lesson2.ui.main.model

import java.util.concurrent.locks.Condition

data class WeatherDTO(
    val fact: FactDTO?
) {
    data class FactDTO (
        val temp: Int?,
        val feels_like: Int?,
        val condition: String?
    )
}