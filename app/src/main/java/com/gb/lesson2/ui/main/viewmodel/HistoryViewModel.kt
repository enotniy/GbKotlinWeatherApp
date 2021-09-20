package com.gb.lesson2.ui.main.viewmodel

import androidx.lifecycle.ViewModel
import com.gb.lesson2.ui.main.model.LocalRepositoryImpl
import com.gb.lesson2.ui.main.model.database.HistoryEntity
import com.gb.lesson2.ui.main.view.App

class HistoryViewModel : ViewModel() {

    private val historyRepository = LocalRepositoryImpl(App.getHistoryDao())

    fun getAllHistory(): List<HistoryEntity> = historyRepository.getAllHistory()

}