package com.gb.lesson2.ui.main.model.database

import android.database.Cursor
import androidx.room.*

@Dao
interface HistoryDao {

    @Query("SELECT * FROM HistoryEntity ORDER BY timestamp DESC")
    fun all(): List<HistoryEntity>

    @Query("SELECT * FROM HistoryEntity WHERE city LIKE :city")
    fun getDataByWord(city: String): List<HistoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(entity: HistoryEntity)

    @Update
    fun update(entity: HistoryEntity)

    @Delete
    fun delete(entity: HistoryEntity)

    @Query("SELECT id, city, temperature FROM HistoryEntity ORDER BY timestamp DESC")
    fun getHistoryCursor(): Cursor

    @Query("SELECT id, city, temperature FROM HistoryEntity WHERE id = :id")
    fun getHistoryCursor(id: Long): Cursor

    @Query("DELETE FROM HistoryEntity WHERE id = :id")
    fun deleteById(id: Long)

}