package com.example.pokedex.data.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationClient {
    // Location updates in specific time interval
    fun getLocationUpdates(interval: Long): Flow<Location>
}