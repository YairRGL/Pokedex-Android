package com.example.pokedex.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import android.Manifest
import android.app.ActivityManager
import android.app.Service

// Use of extension function
fun Context.hasLocationPermission(): Boolean {
    return ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
}

fun Context.isMyServiceRunning(serviceClass: Class<out Service>) = try {
    (getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        .getRunningServices(Int.MAX_VALUE)
        .any { it.service.className == serviceClass.name }
} catch (e: Exception) {
    false
}