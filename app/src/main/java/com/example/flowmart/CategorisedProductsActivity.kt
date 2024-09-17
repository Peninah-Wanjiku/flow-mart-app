package com.example.flowmart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flowmart.api.APIClient
import com.example.flowmart.databinding.ActivityCategorisedProductsBinding

class CategorisedProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategorisedProductsBinding
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategorisedProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        val apiClient = APIClient.getInstance(this)
       // apiClient.post()

        binding.floatingActionBar.setOnClickListener {
            startActivity(Intent(mContext, CreateCategoryActivity::class.java))
        }
    }
}