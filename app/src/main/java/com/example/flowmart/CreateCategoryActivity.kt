package com.example.flowmart

import android.content.Context
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.flowmart.databinding.ActivityCreateCategoryBinding

class CreateCategoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateCategoryBinding
    private  lateinit var mContext: Context
    private lateinit var adapter: ArrayAdapter<String>
    private val categoryList = mutableListOf("")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateCategoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

//        var spinner = binding.categorySpinner
//        adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categoryList)
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
//        spinner.adapter = adapter

        binding.saveCategoryButton.setOnClickListener {
            addCategory()
        }
    }

    private fun addCategory(){
        val categoryInput = binding.categoryInput
        if(categoryInput.text.toString().isEmpty()){
            binding.categoryInput.error = "required!!"
        }else{
            categoryList.add(categoryInput.toString())
            adapter.notifyDataSetChanged()
            categoryInput.text?.clear()
        }
    }
}