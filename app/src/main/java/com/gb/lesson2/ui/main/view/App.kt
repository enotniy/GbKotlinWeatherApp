package com.gb.lesson2.ui.main.view

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.gb.lesson2.ui.main.model.database.HistoryDao
import com.gb.lesson2.ui.main.model.database.HistoryDataBase
import com.google.firebase.messaging.FirebaseMessaging
import java.lang.IllegalStateException

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appInstance = this

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.d("MyFMessagingService", "token = ${task.result.toString()}")
            }
        }
    }

    companion object {
        private var appInstance: App? = null
        private var db: HistoryDataBase? = null

        private const val DB_NAME = "History.db"

        fun getHistoryDao(): HistoryDao {
            if (db == null) {
                if (appInstance == null) throw IllegalStateException("WTF?!!!")

                db = Room.databaseBuilder(
                    appInstance!!,
                    HistoryDataBase::class.java,
                    DB_NAME
                ).allowMainThreadQueries()
                    .build()
            }

            return db!!.historyDao()
        }
    }
}