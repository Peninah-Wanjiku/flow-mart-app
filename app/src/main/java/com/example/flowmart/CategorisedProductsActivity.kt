package com.example.flowmart

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.flowmart.adapters.CategoryAdapter
import com.example.flowmart.data.api.APIClient
import com.example.flowmart.data.models.Category
import com.example.flowmart.databinding.ActivityCategorisedProductsBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategorisedProductsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCategorisedProductsBinding
    private lateinit var mContext: Context
    private val apiClient: APIClient by lazy { APIClient.getInstance(this) }
    private val adapter: CategoryAdapter by lazy { CategoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCategorisedProductsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        binding.floatingActionBar.setOnClickListener {
            startActivity(Intent(mContext, CreateCategoryActivity::class.java))
        }

        binding.recyclerviewCategory.adapter = adapter
    }
    override fun onResume() {
        super.onResume()
        fetchCategories()
    }

    @SuppressLint("SetTextI18n")
    private fun fetchCategories() {
        binding.progressIndicator.visibility = View.VISIBLE
        binding.recyclerviewCategory.visibility = View.GONE
        binding.txtMessage.visibility = View.GONE

        // Fetch categories from the API using the APIClient
        lifecycleScope.launch(Dispatchers.IO) {
            apiClient.get(
                endpoint = "categories",
                successListener = { jsonResponse ->
                    val categories = jsonResponse.getJSONArray("categories")
                    val categoryList = mutableListOf<Category>()
                    for (i in 0 until categories.length()) {
                        val category = categories.getJSONObject(i)
                        val id = category.getInt("id")
                        val name = category.getString("name")
                        categoryList.add(Category(id, name))
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.progressIndicator.visibility = View.GONE
                        if (categoryList.isEmpty()) {
                            binding.txtMessage.visibility = View.VISIBLE
                            binding.txtMessage.text = "No categories found"
                        } else {
                            binding.recyclerviewCategory.visibility = View.VISIBLE
                            adapter.addCategories(categoryList)
                        }
                    }
                },
                errorListener = { error ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.progressIndicator.visibility = View.GONE
                        binding.txtMessage.visibility = View.VISIBLE
                        binding.txtMessage.text = error.getString("message")
                    }
                }
            )
        }
    }
}