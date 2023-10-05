package com.example.pokedex.view.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pokedex.R
import com.example.pokedex.databinding.ActivityHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView


class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_map
            )
        )

        //Open from notification
        val fragmentName = intent.extras?.getString("fragmentName")
        val data = intent.extras?.getString("data", "")
        when (fragmentName) {
            "MapsFragment" -> navController.navigate(R.id.navigation_map)
            "HomeFragment" -> {
                val bundle = bundleOf("data" to data)
                navController.navigate(R.id.navigation_home, bundle)
            }
        }



        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }




}