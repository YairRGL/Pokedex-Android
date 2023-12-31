package com.example.pokedex.view.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.pokedex.R
import com.example.pokedex.view.ui.activities.HomeActivity
import com.example.pokedex.viewmodel.LoginViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class LoginFragment : Fragment() {

    companion object {
        fun newInstance() = LoginFragment()
    }

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        val btnLogin = view.findViewById(R.id.btnLogin) as Button
        val txtInputLayoutUsername = view.findViewById(R.id.txtInputUsername) as TextInputLayout
        val txtUsername = view.findViewById(R.id.editTxtInputUsername) as TextInputEditText

        btnLogin.setOnClickListener{
            val username = txtUsername.text.toString()
            viewModel.validateAndSaveUsername(username)
        }

        viewModel.isUsernameValid.observe(viewLifecycleOwner) { isValid ->
            if (isValid) {
                launchHomeActivity()
            } else {
                Toast.makeText(activity?.applicationContext, "Compruebe su nombre de usuario.", Toast.LENGTH_LONG).show()
            }
        }

        viewModel.validationMsg.observe(viewLifecycleOwner) { validationMsg ->
            if (!validationMsg.isBlank()) {
                txtInputLayoutUsername.error = validationMsg
            }
        }

        txtUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                txtInputLayoutUsername.error = null
            }

            override fun afterTextChanged(s: Editable?) {
            }
        })

    }

    fun launchHomeActivity(){
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }

}