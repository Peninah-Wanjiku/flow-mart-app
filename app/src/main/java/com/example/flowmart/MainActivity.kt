package com.example.flowmart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.flowmart.data.SharedPreferenceManager
import com.example.flowmart.data.api.APIClient
import com.example.flowmart.data.models.User
import com.example.flowmart.databinding.ActivityMainBinding
import org.json.JSONObject

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mContext: Context
    private lateinit var currentUser: User

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

        binding.recyclerviewChips.setOnClickListener {
            val chip = binding.recyclerviewChips.id

            val apiClient = APIClient.getInstance(this)
            val jsonRequest = JSONObject()
            jsonRequest.put("category_id", chip)
        }
        binding.recyclerviewItems.setOnClickListener {

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