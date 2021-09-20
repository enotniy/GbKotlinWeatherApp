package com.gb.lesson2.ui.main.model

import com.gb.lesson2.ui.main.model.database.HistoryDao
import com.gb.lesson2.ui.main.model.database.HistoryEntity

interface LocalRepository {
    fun getAllHistory(): List<HistoryEntity>
    fun saveEntity(weather: HistoryEntity)
}