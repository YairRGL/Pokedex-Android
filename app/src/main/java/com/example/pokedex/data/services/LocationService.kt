package com.example.pokedex.data.services

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.pokedex.R
import com.example.pokedex.data.UserSessionManager
import com.example.pokedex.data.location.LocationClient
import com.example.pokedex.data.location.LocationManager
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {
    private lateinit var locationClient: LocationClient
    private lateinit var userSessionManager: UserSessionManager

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        userSessionManager = UserSessionManager(applicationContext)
        locationClient = LocationManager(
            applicationContext,
            LocationServices.getFusedLocationProviderClient(applicationContext)
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action){
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }

        return super.onStartCommand(intent, flags, startId)
    }

    private fun start(){
        val notification = NotificationCompat.Builder(this, "location")
            .setContentTitle("Pokedex")
            .setContentTitle("Atrapalos a todos ${userSessionManager.getUser()?.username}!")
            .setSmallIcon(R.drawable.ic_notification_pokeball)
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient.getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location  ->
                val lat = location.latitude.toString().takeLast(4)
                val long = location.longitude.toString().takeLast(4)
                val updateNotification = notification.setContentText(
                    "Atrapalos a todos ${userSessionManager.getUser()?.username} !, Location: ($lat, $long)"
                )
                notificationManager.notify(100, updateNotification.build())
            }

        startForeground(100, notification.build())
    }

    private fun stop(){
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }


    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}