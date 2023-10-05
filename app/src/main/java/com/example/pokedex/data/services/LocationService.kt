package com.example.pokedex.data.services

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.pokedex.R
import com.example.pokedex.data.session.UserSessionManager
import com.example.pokedex.data.location.LocationClient
import com.example.pokedex.data.location.LocationManager
import com.example.pokedex.view.ui.activities.HomeActivity
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class LocationService: Service() {
    private lateinit var locationClient: LocationClient
    private lateinit var userSessionManager: UserSessionManager
    private val serviceScope = CoroutineScope(SupervisorJob())

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
            .setContentTitle("Buscando...")
            .setContentText("Atrapalos a todos, ${userSessionManager.getUser()?.username?.uppercase()}!")
            .setSmallIcon(R.drawable.ic_notification_pokeball)
            .setOngoing(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            notification.foregroundServiceBehavior = Notification.FOREGROUND_SERVICE_IMMEDIATE
        }

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        var isFirstLocationUpdate = true
        locationClient.getLocationUpdates(10000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location  ->
                if (!isFirstLocationUpdate) {
                    val intent = Intent(this, HomeActivity::class.java)
                    intent.putExtra("fragmentName", "HomeFragment")
                    intent.putExtra("data", "newPokemon")

                    val flags = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                        PendingIntent.FLAG_IMMUTABLE
                    } else {
                        PendingIntent.FLAG_UPDATE_CURRENT
                    }

                    val pendingIntent = PendingIntent.getActivity(this, 0, intent, flags)

                    val notificationPokemonFound = NotificationCompat.Builder(this, "location")
                        .setContentTitle("Has encontrado un nuevo Pokémon")
                        .setContentText("¡Descubre quién es!")
                        .setAutoCancel(true)
                        .setSmallIcon(R.drawable.ic_notification_pokeball_open)
                        .setVibrate(longArrayOf(100, 200, 300, 400))
                        .setContentIntent(pendingIntent)

                    notificationManager.notify(101, notificationPokemonFound.build())
                }
                isFirstLocationUpdate = false
            }.launchIn(serviceScope)

        startForeground(100, notification.build())
    }

    private fun stop(){
        stopForeground(STOP_FOREGROUND_REMOVE)
        stopSelf()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }

}