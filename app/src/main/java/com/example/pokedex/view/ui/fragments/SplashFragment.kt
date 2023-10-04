package com.example.pokedex.view.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.pokedex.R
import com.example.pokedex.data.UserSessionManager
import com.example.pokedex.view.ui.activities.HomeActivity

class SplashFragment : Fragment(R.layout.fragment_splash) {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val userSessionManager = UserSessionManager(view.context.applicationContext)

        Handler(Looper.getMainLooper()).postDelayed({
            if(userSessionManager.isLoggedIn()){
                launchHomeActivity()
            } else {
                findNavController().navigate(R.id.navigate_splashFragment_to_loginFragment)
            }
        }, 3500)

    }

    fun launchHomeActivity(){
        Toast.makeText(activity?.applicationContext, "Iniciando...", Toast.LENGTH_SHORT).show()
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}