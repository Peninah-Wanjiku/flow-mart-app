package com.example.flowmart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.flowmart.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        val sharedPreferences = getSharedPreferences("userPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding.signUpButton.setOnClickListener {
            val name = binding.nameInput.text.toString()
            val email = binding.emailInput.text.toString()
            val phone = binding.phoneInput.text.toString()
            val password = binding.passwordInput.text.toString()

            if (isValidInput(name, email, phone, password, password)) {
                editor.putString("username", name)
                editor.putString("email", email)
                editor.putString("phone", phone)
                editor.putString("password", password)
                editor.apply()

                navigateToLoginActivity()

            }else{
                Toast.makeText(mContext, "All fields required", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isValidInput(name: String, email: String, phone: String, password: String, confirmPassword: String): Boolean {
        if (password != confirmPassword){
            Toast.makeText(mContext, "Password and confirm password must match", Toast.LENGTH_SHORT).show()
            return false
        }else {
            return name.isNotEmpty() && email.isNotEmpty() && phone.isNotEmpty() && password.isNotEmpty() && confirmPassword.isNotEmpty()
        }
    }

    private fun navigateToLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
        finish()
    }
}