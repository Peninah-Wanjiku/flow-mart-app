package com.example.flowmart

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flowmart.databinding.ActivityCreateCategoryBinding
import com.example.flowmart.databinding.ActivityCreateItemBinding

class CreateItemActivity : AppCompatActivity() {
    private  lateinit var binding: ActivityCreateItemBinding
    private lateinit var mContext: Context


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        binding.saveItemButton.setOnClickListener {
            var itemName = binding.itemName.text.toString()
            var quantity = binding.itemQuantity.text.toString()

            if (itemName.isEmpty() || quantity.isEmpty()){
                Toast.makeText(mContext, "All fields are required", Toast.LENGTH_SHORT).show()
            }else{

            }
        }

    }
}