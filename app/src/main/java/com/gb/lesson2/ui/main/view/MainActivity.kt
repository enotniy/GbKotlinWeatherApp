package com.gb.lesson2.ui.main.view

import android.Manifest
import android.content.Intent
import android.database.Cursor
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.gb.lesson2.R
import com.gb.lesson2.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {

    private val permissionResult = registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
        when {
            result ->  getContact()
            !ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CONTACTS) -> {
                Toast.makeText(this, "Go to app settings and enable permission", Toast.LENGTH_LONG).show()
            }
            else ->  Toast.makeText(this, "T_T", Toast.LENGTH_LONG).show()
        }
    }

    private val binding: MainActivityBinding by lazy {
        MainActivityBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.idHistory -> {
                supportFragmentManager.apply {
                    beginTransaction()
                        .replace(R.id.container, HistoryFragment())
                        .addToBackStack("")
                        .commitAllowingStateLoss()
                }
                true
            }
            R.id.getContacts -> {
                permissionResult.launch(Manifest.permission.READ_CONTACTS)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getContact() {
        val cursor : Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        )

        val contacts = mutableListOf<String>()
        cursor?.let {
            for (i in 0..cursor.count) {
                if (cursor.moveToPosition(i)) {
                    val name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

                    contacts.add(name)
                }
            }

            it.close()
        }


        AlertDialog.Builder(this)
            .setItems(contacts.toTypedArray()) { _, _ -> /* do noting */ }
            .setCancelable(true)
            .show()

    }
}