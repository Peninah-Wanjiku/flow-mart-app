package com.example.flowmart

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.flowmart.data.api.APIClient
import com.example.flowmart.databinding.ActivityEditProfileBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var mContext: Context
    private val apiClient: APIClient by lazy { APIClient.getInstance(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        val toolbar = binding.toolbar
        setSupportActionBar(toolbar)
        supportActionBar?.title = ""

        fetchUserDetails()

        binding.saveProfileButton.setOnClickListener {

        }

    }
    private fun fetchUserDetails(){
        binding.progressIndicator.visibility = View.VISIBLE
        binding.main.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            try {
                apiClient.get(
                    endpoint = "profile",
                    successListener = { jsonResponse ->
                        // Switch to the main thread for UI updates
                        lifecycleScope.launch(Dispatchers.Main) {
                            binding.progressIndicator.visibility = View.GONE
                            binding.main.visibility = View.VISIBLE
                            // Handle success
                            if (jsonResponse["status"] == "success") {
                                val user = jsonResponse.getJSONObject("user")
                                val name = user.getString("name")
                                val email = user.getString("email")
                                val phone = user.getString("phone")
                                // val password = user.getString("password")

                                // Update UI with user details
                                binding.nameText.text = name
                                binding.emailText.text = email
                                binding.phoneText.text = phone
                                // binding.passwordText.text = password
                            }
                        }
                    },
                    errorListener = { error ->
                        // Switch to the main thread for UI updates
                        lifecycleScope.launch(Dispatchers.Main) {
                            binding.progressIndicator.visibility = View.GONE
                            binding.main.visibility = View.VISIBLE
                            // Handle error
                            val errorMessage = error["message"] as String

                            // Update UI with error message
                            binding.nameText.text = errorMessage
                            binding.emailText.text = errorMessage
                            binding.phoneText.text = errorMessage
                            binding.passwordText.text = errorMessage
                        }
                    }
                )
            } catch (e: Exception) {
                // Handle exception (optional)
                withContext(Dispatchers.Main) {
                    binding.progressIndicator.visibility = View.GONE
                    binding.main.visibility = View.VISIBLE
                    // Display error message
                }
            }
        }


    }

}