package com.example.flowmart

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.flowmart.adapters.ChipAdapter
import com.example.flowmart.adapters.ProductAdapter
import com.example.flowmart.data.SharedPreferenceManager
import com.example.flowmart.data.api.APIClient
import com.example.flowmart.data.models.Category
import com.example.flowmart.data.models.Product
import com.example.flowmart.data.models.User
import com.example.flowmart.databinding.ActivityMainBinding
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mContext: Context
    private lateinit var currentUser: User
    private val apiClient: APIClient by lazy { APIClient.getInstance(this) }
    private val productAdapter: ProductAdapter by lazy { ProductAdapter() }
    private val chipAdapter: ChipAdapter by lazy { ChipAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        currentUser = SharedPreferenceManager.getInstance(this).getUser()
        binding.nameWelcome.text = currentUser.name

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        binding.floatingActionBar.setOnClickListener {
            startActivity(Intent(mContext, CreateItemActivity::class.java))
        }

        binding.recyclerviewItems.adapter = productAdapter
        binding.recyclerviewChips.adapter = chipAdapter

    }

    override fun onResume() {
        super.onResume()
        fetchProducts()
        fetchCategories()
    }

    private fun fetchProducts() {
        binding.progressIndicator.visibility = View.VISIBLE
        binding.recyclerviewItems.visibility = View.GONE
        binding.txtMessage.visibility = View.GONE

        lifecycleScope.launch(Dispatchers.IO) {
            apiClient.get(
                endpoint = "products",
                successListener = { jsonResponse ->
                    val products = jsonResponse.getJSONArray("products")
                    val productList = mutableListOf<Product>()
                    for (i in 0 until products.length()) {
                        // Parse product details
                        val product = products.getJSONObject(i)
                        val id = product.getInt("id")
                        val name = product.getString("name")
                        val quantity = product.getString("quantity")

                        // Parse category details
                        val category = product.getJSONObject("category")
                        val categoryId = category.getInt("id")
                        val categoryName = category.getString("name")

                        // Create a Product object and add it to the list
                        productList.add(Product(id, name, quantity, Category(categoryId, categoryName)))
                    }
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.progressIndicator.visibility = View.GONE
                        if (productList.isEmpty()) {
                            binding.txtMessage.visibility = View.VISIBLE
                            binding.txtMessage.text = "No products found"
                        } else {
                            binding.recyclerviewItems.visibility = View.VISIBLE
                            productAdapter.addProducts(productList)
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

    @SuppressLint("SetTextI18n")
    private fun fetchCategories() {
        binding.progressIndicator.visibility = View.VISIBLE
        binding.recyclerviewChips.visibility = View.GONE
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
                            binding.recyclerviewChips.visibility = View.VISIBLE
                            chipAdapter.addCategories(categoryList)
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu, menu)
        return super.onCreateOptionsMenu(menu)

    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.profile -> startActivity(Intent(mContext, EditProfileActivity::class.java))
            R.id.create_category -> startActivity(
                Intent(
                    mContext,
                    CategorisedProductsActivity::class.java
                )
            )
        }
        return super.onOptionsItemSelected(item)

    }
}