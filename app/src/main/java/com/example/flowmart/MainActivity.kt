package com.example.flowmart

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.flowmart.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mContext: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mContext = this

        setSupportActionBar(binding.toolbar)
        supportActionBar?.title = ""

        binding.floatingActionBar.setOnClickListener {
            startActivity(Intent(mContext, CreateItemActivity::class.java))
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_activity_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.profile -> startActivity(Intent(mContext, EditProfileActivity::class.java))
            R.id.create_category -> startActivity(Intent(mContext, CategorisedProductsActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }
}