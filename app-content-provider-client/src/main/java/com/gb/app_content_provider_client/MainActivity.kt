package com.gb.app_content_provider_client

import android.content.ContentResolver
import android.content.ContentUris
import android.database.Cursor
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.gb.app_content_provider_client.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val historySource = HistorySource(contentResolver)
        historySource.query()

        binding.get.setOnClickListener {
            val list = historySource.getHistory()

            AlertDialog.Builder(this)
                .setItems(list.map {
                    "${it.id} - ${it.city} - ${it.temperature}"
                }.toTypedArray()) { _, whitch ->
                    historySource.delete(HistoryEntity(list[whitch].id))
                }
                .setCancelable(true)
                .show()
        }

    }
}

private val HISTORY_URI: Uri = Uri.parse("content://geekbrains.provider/HistoryEntity")

class HistorySource(val contentResolver: ContentResolver) {
    private var cursor: Cursor? = null

    fun query() {
        cursor = contentResolver.query(HISTORY_URI, null, null, null, null)
        Log.d("MainActivity", cursor.toString())
    }

    fun getHistory(): List<HistoryEntity> {
        val list = mutableListOf<HistoryEntity>()
        cursor?.let { cursor ->
            for (i in 0..cursor.count) {
                if (cursor.moveToPosition(i)) {
                    list.add(toEntity(cursor))
                }
            }
        }
        cursor?.close()
        query()
        return list
    }

    fun delete(entity: HistoryEntity) {
        val uri: Uri = ContentUris.withAppendedId(HISTORY_URI, entity.id)
        contentResolver.delete(uri, null, null)
        query()
    }

    fun toEntity(cursor: Cursor): HistoryEntity {
        return HistoryEntity(
            cursor.getLong(cursor.getColumnIndex(ID)),
            cursor.getString(cursor.getColumnIndex(CITY)),
            cursor.getInt(cursor.getColumnIndex(TEMPERATURE))
        )
    }

}