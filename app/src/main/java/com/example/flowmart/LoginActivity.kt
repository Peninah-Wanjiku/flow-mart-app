package com.example.flowmart

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flowmart.data.SharedPreferenceManager
import com.example.flowmart.data.api.APIClient
import com.example.flowmart.databinding.ActivityLoginBinding
import com.example.flowmart.utils.LoadingDialog
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        binding.txtSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.loginButton.setOnClickListener {
            val email = binding.phoneEmailInput.text.toString()
            val password = binding.passwordInput.text.toString()

            val apiClient = APIClient.getInstance(this)
            val jsonRequest = JSONObject()
            jsonRequest.put("email", email)
            jsonRequest.put("password", password)

            LoadingDialog.show(this, "Logging in...")

            apiClient.post(
                endpoint = "login",
                requestBody = jsonRequest,
                successListener = { jsonResponse ->
                    LoadingDialog.hide()
                    if (jsonResponse["status"] == "success") {
                        val apiKey = jsonResponse["api_key"] as String
                        SharedPreferenceManager.getInstance(this).saveAPIKey(apiKey)
                        navigateToMainActivity()
                    } else {
                        Toast.makeText(mContext, "Error: ${jsonResponse["message"]}", Toast.LENGTH_SHORT).show()
                    }
                },
                errorListener = { error ->
                    LoadingDialog.hide()
                    Toast.makeText(mContext, "Error: ${error["message"]}", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

     private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}