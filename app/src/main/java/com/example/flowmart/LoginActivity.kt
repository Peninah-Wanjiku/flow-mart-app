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
import com.example.flowmart.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)

        binding.loginButton.setOnClickListener {
            val enteredEmail = binding.phoneEmailInput.text.toString()
            val enteredPhone = binding.phoneEmailInput.text.toString()
            val enteredPassword = binding.passwordInput.text.toString()

            val savedPhone = sharedPreferences.getString("phone", null)
            val savedEmail = sharedPreferences.getString("email", null)
            val savedPassword = sharedPreferences.getString("password", null)

            if (enteredEmail == savedEmail || enteredPhone == savedPhone) {
                if (enteredPassword == savedPassword) {
                    val editor = sharedPreferences.edit()
                    editor.putBoolean("isLoggedIn", true)
                    editor.apply()

                    navigateToMainActivity()
                }
            } else {
                Toast.makeText(mContext, "Invalid credentials", Toast.LENGTH_SHORT).show()
            }
        }
    }

     private fun navigateToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}