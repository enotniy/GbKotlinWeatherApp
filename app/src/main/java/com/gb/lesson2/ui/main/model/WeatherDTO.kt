package com.gb.lesson2.ui.main.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

data class WeatherDTO(
    val fact: FactDTO?
) {
    @Parcelize
    data class FactDTO (
        val temp: Int?,
        val feels_like: Int?,
        val condition: String?
    ): Parcelable
}