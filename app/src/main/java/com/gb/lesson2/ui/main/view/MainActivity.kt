package com.gb.lesson2.ui.main.view

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.database.Cursor
import android.graphics.Color
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.gb.lesson2.MapsFragment
import com.gb.lesson2.R
import com.gb.lesson2.databinding.MainActivityBinding
import com.gb.lesson2.ui.main.model.MyFirebaseMessagingService
import java.io.IOException

private const val REFRESH_PERIOD = 60000L
private const val MINIMAL_DISTANCE = 100f

class MainActivity : AppCompatActivity() {

    private val permissionResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> getContact()
                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_CONTACTS
                ) -> {
                    Toast.makeText(
                        this,
                        "Go to app settings and enable permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> Toast.makeText(this, "T_T", Toast.LENGTH_LONG).show()
            }
        }

    private val permissionGeoResult =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { result ->
            when {
                result -> getLocation()

                !ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) -> {
                    Toast.makeText(
                        this,
                        "Go to app settings and enable permission",
                        Toast.LENGTH_LONG
                    ).show()
                }
                else -> Toast.makeText(this, "T_T", Toast.LENGTH_LONG).show()
            }
        }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        val locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager


        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locationManager.getProvider(LocationManager.GPS_PROVIDER)?.let {
                locationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER,
                    REFRESH_PERIOD,
                    MINIMAL_DISTANCE,
                    object : LocationListener {

                        override fun onLocationChanged(location: Location) {
                            getAddressByLocation(location)
                        }

                        override fun onStatusChanged(
                            provider: String?,
                            status: Int,
                            extras: Bundle?
                        ) {
                            // do noting
                        }

                        override fun onProviderEnabled(provider: String) {

                        }

                        override fun onProviderDisabled(provider: String) {

                        }
                    }
                )
            }
        } else {
            locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)?.let {
                getAddressByLocation(it)
            } ?: {  /* message for user */ }
        }

    }

    private fun getAddressByLocation(location: Location) {
        val geocoder = Geocoder(this)

        Thread {
            try {
                val address = geocoder.getFromLocation(
                    location.latitude,
                    location.longitude,
                    1
                )

                binding.container.post {
                    AlertDialog.Builder(this)
                        .setMessage(address[0].getAddressLine(0))
                        .setCancelable(true)
                        .show()
                }

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }.start()

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
            R.id.getLocations -> {
                permissionGeoResult.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                true
            }
            R.id.showMaps -> {
                handleDataMessage(
                    mapOf(
                        "title" to "My title",
                        "message" to "my message"
                    )
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getContact() {
        val cursor: Cursor? = contentResolver.query(
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
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))

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

    private fun handleDataMessage(remoteMessageData: Map<String, String>) {
        val title = remoteMessageData[MyFirebaseMessagingService.PUSH_KEY_TITLE]
        val message = remoteMessageData[MyFirebaseMessagingService.PUSH_KEY_MESSAGE]

        if (!title.isNullOrBlank() && !message.isNullOrBlank()) {

            val notification = NotificationCompat.Builder(this,
                MyFirebaseMessagingService.CHANNEL_ID
            )
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setColor(Color.BLUE)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                notificationManager.createNotificationChannel(
                    NotificationChannel(
                        MyFirebaseMessagingService.CHANNEL_ID,
                        "First Channel",
                        NotificationManager.IMPORTANCE_DEFAULT
                    ).apply {
                        description = "Description"
                    }
                )
            }

            notificationManager.notify(MyFirebaseMessagingService.NOTIFICATION_ID, notification.build())
        }
    }

}