package com.example.flowmart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flowmart.data.api.APIClient
import com.example.flowmart.databinding.ActivitySignUpBinding
import com.example.flowmart.utils.LoadingDialog
import org.json.JSONObject

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        binding.signUpButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val phone = binding.phoneInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (isValidInput(name, email, phone, password, password)) {
                val apiClient = APIClient.getInstance(this)
                val jsonRequest = JSONObject()
                jsonRequest.put("name", name)
                jsonRequest.put("email", email)
                jsonRequest.put("phone", phone)
                jsonRequest.put("password", password)

                LoadingDialog.show(this, "Signing up...")

                apiClient.post(
                    endpoint = "register",
                    requestBody = jsonRequest,
                    successListener = { jsonResponse ->
                        LoadingDialog.hide()
                        if(jsonResponse["status"]=="success"){
                            finish()
                        }else{
                            Toast.makeText(mContext, "Error: ${jsonResponse["message"]}", Toast.LENGTH_SHORT).show()
                        }
                    },
                    errorListener = { error ->
                        LoadingDialog.hide()
                        Toast.makeText(mContext, "Error: ${error["message"]}", Toast.LENGTH_SHORT).show()
                    })

            } else {
                Toast.makeText(mContext, "All fields required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidInput(
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ): Boolean {
        if (password != confirmPassword) {
            Toast.makeText(mContext, "Password and confirm password must match", Toast.LENGTH_SHORT)
                .show()
            return false
        } else {
            return name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
        }
    }
}