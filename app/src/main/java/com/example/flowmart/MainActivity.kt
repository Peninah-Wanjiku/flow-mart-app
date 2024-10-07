package com.example.flowmart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
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
    private lateinit var chipGroup: ChipGroup
    private val apiClient: APIClient by lazy { APIClient.getInstance(this) }
    private val productAdapter: ProductAdapter by lazy { ProductAdapter() }
    //private val chipAdapter: ChipAdapter by lazy { ChipAdapter() }

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


//        fetchCategoriesAndPopulateChips()
        binding.recyclerviewItems.setOnClickListener {

        }
    }

    override fun onResume() {
        super.onResume()
        fetchProducts()
//        fetchCategoriesAndPopulateChips()
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
//    private fun populateChips(categories: List<Category>) {
//        chipGroup.removeAllViews()
//
//        categories.forEach { category ->
//            val chip = Chip(this)
//            chip.text = category.name
//            chip.isClickable = true
//            chip.isCheckable = true
//
//            // Add chip to the ChipGroup
//            chipGroup.addView(chip)
//        }
//    }
//    private fun fetchCategoriesAndPopulateChips() {
//
//        CoroutineScope(Dispatchers.IO).launch {
//            try {
//                val categories = apiClient.get()
//
//                withContext(Dispatchers.Main) {
//                    populateChips(categories)
//                }
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }
//    }


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