package com.example.pokedex.model

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class UserSessionManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()
    private val gson = Gson()

    companion object {
        private const val PREF_NAME = "UserSession"
        private const val PRIVATE_MODE = 0
        private const val KEY_USER = "user"
    }

    fun saveUser(user: User) {
        val userJson = gson.toJson(user)
        editor.putString(KEY_USER, userJson)
        editor.apply()
    }

    fun getUser(): User? {
        val userJson = sharedPreferences.getString(KEY_USER, null)
        return gson.fromJson(userJson, User::class.java)
    }

    fun isLoggedIn(): Boolean {
        return getUser() != null
    }

    fun logout() {
        editor.remove(KEY_USER)
        editor.apply()
    }
}

