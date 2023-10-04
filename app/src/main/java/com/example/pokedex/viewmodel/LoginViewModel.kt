package com.example.pokedex.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pokedex.data.model.User
import com.example.pokedex.data.UserSessionManager

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val userSessionManager = UserSessionManager(application)
    private val isUsernameValidVM = MutableLiveData<Boolean>()
    val isUsernameValid: LiveData<Boolean> get() = isUsernameValidVM
    private val validationMsgVM = MutableLiveData<String>()
    val validationMsg: LiveData<String> get() = validationMsgVM

    fun validateAndSaveUsername(username: String) {
        if(username.isBlank()){
            validationMsgVM.value = "El nombre de usuario no puede estar vacio."
            isUsernameValidVM.value = false
        } else if(username.length>20){
            validationMsgVM.value = "El nombre de usuario no debe ser mayor a 20 caracteres."
            isUsernameValidVM.value = false
        } else if(username.length<3){
            validationMsgVM.value = "El nombre de usuario debe ser mayor a 3 caracteres."
        } else {
            validationMsgVM.value = ""
            isUsernameValidVM.value = true
            val user = User(username)
            userSessionManager.saveUser(user)
        }
    }


}