package com.example.flowmart

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flowmart.data.api.APIClient
import com.example.flowmart.databinding.ActivityCreateCategoryBinding
import com.example.flowmart.databinding.ActivityCreateItemBinding
import org.json.JSONObject

class CreateItemActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityCreateItemBinding
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        binding.saveItemButton.setOnClickListener {
            createItem()
        }

    }

    private fun createItem() {
        val itemName = binding.itemName.text.toString()
        val quantity = binding.itemQuantity.text.toString()
        val spinnerValue = binding.categorySpinner.selectedItem.toString()

        if (itemName.isNotEmpty() || quantity.isNotEmpty() || spinnerValue.isNotEmpty()){
            val apiClient = APIClient.getInstance(this)
            val jsonRequest = JSONObject()
            jsonRequest.put("name", itemName)
            jsonRequest.put("quantity", quantity)
            jsonRequest.put("category", spinnerValue)

            apiClient.post(
                endpoint = "products",
                requestBody = jsonRequest,
                successListener = { jsonResponse ->
                    if (jsonResponse["status"] == "success") {
                        Toast.makeText(this, "Item added successfully", Toast.LENGTH_SHORT).show()
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

        }else{
            Toast.makeText(mContext, "All fields are required", Toast.LENGTH_SHORT).show()
        }
    }
}