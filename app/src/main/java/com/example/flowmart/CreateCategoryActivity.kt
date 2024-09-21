package com.example.flowmart

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flowmart.data.api.APIClient
import com.example.flowmart.data.models.Category
import com.example.flowmart.databinding.ActivityCreateCategoryBinding
import com.example.flowmart.utils.LoadingDialog
import org.json.JSONObject

class CreateCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCategoryBinding
    private lateinit var mContext: Context
    private lateinit var adapter: ArrayAdapter<String>
    private val categoryList = mutableListOf<Category>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        binding.saveCategoryButton.setOnClickListener {
            addCategory()
        }
    }

    private fun addCategory() {
        val categoryInput = binding.categoryInput.text.toString()
        if (categoryInput.isNotEmpty()){

            val apiClient = APIClient.getInstance(this)
            val jsonRequest = JSONObject()

            jsonRequest.put("name", categoryInput)

            apiClient.post(
                endpoint = "categories",
                requestBody = jsonRequest,
                successListener = { jsonResponse ->
                    if (jsonResponse["status"] == "success") {
                        Toast.makeText(this, "Category added successfully", Toast.LENGTH_SHORT).show()
                        finish()
                    } else {
                        Toast.makeText(
                            mContext,
                            "Error: ${jsonResponse["message"]}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                errorListener = { error ->
                    Toast.makeText(mContext, "Error: ${error["message"]}", Toast.LENGTH_SHORT)
                        .show()
                })

        }
    }
}
