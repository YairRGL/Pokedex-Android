package com.example.pokedex.view.ui.fragments.map

import android.Manifest
import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.pokedex.R
import com.example.pokedex.data.location.hasLocationPermission
import com.example.pokedex.data.services.LocationService
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment

class MapsFragment : Fragment() {

    @SuppressLint("MissingPermission")
    private val callback = OnMapReadyCallback { googleMap ->
        if (!requireActivity().applicationContext.hasLocationPermission()) {
            launcher.launch(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION))
        } else {
            googleMap.isMyLocationEnabled = true
            googleMap.uiSettings.isMyLocationButtonEnabled =  true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
        val btnSearch = view.findViewById(R.id.btnStartPokemonSearch) as Button

        btnSearch.setOnClickListener{
            val intent = Intent(requireActivity().applicationContext, LocationService::class.java)

            if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.TIRAMISU){
                requestNotificationPermission()
            }

            createChannel()

            // TODO: WIP
            if (isMyServiceRunning()) {
                intent.action = LocationService.ACTION_STOP
                btnSearch.text = "Iniciar búsqueda"
            } else {
                intent.action = LocationService.ACTION_START
                btnSearch.text = "Terminar búsqueda"
            }
            requireActivity().startService(intent)

        }

    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestNotificationPermission() {
        launcher.launch(arrayOf(Manifest.permission.POST_NOTIFICATIONS))
    }

    private fun createChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val channel = NotificationChannel("location", "Location", NotificationManager.IMPORTANCE_HIGH)
            val notificationManager = requireActivity().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    private fun isMyServiceRunning(): Boolean {
        val manager =
            requireActivity().getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (LocationService::class.java.getName() == service.service.className) {
                return true
            }
        }
        return false
    }

    val launcher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val allPermissionsGranted = permissions.values.all { it }

        if (allPermissionsGranted) {
            if(permissions.size>1){
                val navController = findNavController()
                navController.run {
                    popBackStack()
                    navigate(R.id.navigation_map)
                }
            }
        } else {
            // Some or all permissions are denied
        }
    }

}