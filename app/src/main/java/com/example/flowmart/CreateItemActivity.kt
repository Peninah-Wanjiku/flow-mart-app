package com.example.flowmart

//noinspection SuspiciousImport
import android.R
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.flowmart.data.api.APIClient
import com.example.flowmart.data.models.Category
import com.example.flowmart.databinding.ActivityCreateItemBinding
import com.example.flowmart.utils.LoadingDialog
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class CreateItemActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateItemBinding
    private lateinit var mContext: Context
    private val apiClient: APIClient by lazy { APIClient.getInstance(this) }
    private val categoryList: MutableList<Category> by lazy { mutableListOf() }
    private lateinit var selectedCategory: Category


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        binding.saveItemButton.setOnClickListener {
            val itemName = binding.itemName.text.toString()
            val quantity = binding.itemQuantity.text.toString()

            if (itemName.isNotEmpty() && quantity.isNotEmpty()) {
                saveItem(itemName, quantity)
            } else {
                Toast.makeText(mContext, "All fields are required", Toast.LENGTH_SHORT).show()
            }
        }

        binding.categorySpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                selectedCategory = categoryList[p2]
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        fetchCategories()
    }

    private fun saveItem(itemName: String, quantity: String) {
        LoadingDialog.show(this, "Saving item...")

        val jsonRequest = JSONObject()
        jsonRequest.put("category_id", selectedCategory.id)
        jsonRequest.put("name", itemName)
        jsonRequest.put("quantity", quantity)

        apiClient.post(
            endpoint = "products",
            requestBody = jsonRequest,
            successListener = { jsonResponse ->
                LoadingDialog.hide()
                if (jsonResponse["status"] == "success") {
                    Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
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
                LoadingDialog.hide()
                Toast.makeText(mContext, "Error: ${error["message"]}", Toast.LENGTH_SHORT)
                    .show()
            },
        )
    }

    private fun fetchCategories() {
        binding.categorySpinner.visibility = View.GONE
        binding.txtLoading.visibility = View.VISIBLE

        lifecycleScope.launch(Dispatchers.IO) {
            apiClient.get(
                endpoint = "categories",
                successListener = { jsonResponse ->
                    val categories = jsonResponse.getJSONArray("categories")
                    val categoryNames = mutableListOf<String>()
                    for (i in 0 until categories.length()) {
                        val category = categories.getJSONObject(i)
                        val id = category.getInt("id")
                        val name = category.getString("name")
                        categoryNames.add(name)
                        categoryList.add(Category(id, name))
                    }
                    selectedCategory = categoryList[0]
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.categorySpinner.visibility = View.VISIBLE
                        binding.txtLoading.visibility = View.GONE
                        populateSpinner(categoryNames)
                    }
                },
                errorListener = { error ->
                    Toast.makeText(mContext, "Error: ${error["message"]}", Toast.LENGTH_SHORT)
                        .show()
                })
        }
    }

    private fun populateSpinner(items: List<String>) {
        val adapter = ArrayAdapter(this, R.layout.simple_spinner_item, items)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.categorySpinner.adapter = adapter
    }
}