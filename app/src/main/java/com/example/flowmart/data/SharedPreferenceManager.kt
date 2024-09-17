package com.example.flowmart.data

import android.annotation.SuppressLint
import android.content.Context
import com.example.flowmart.data.models.User

/**
 * @Author: Angatia Benson
 * @Date: 9/17/2024
 * Copyright (c) 2024 BanIT
 */
class SharedPreferenceManager private constructor(context: Context) {
    /**
     * Companion object to ensure a single instance of the SharedPreferenceManager
     */
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: SharedPreferenceManager? = null

        fun getInstance(context: Context): SharedPreferenceManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: SharedPreferenceManager(context.applicationContext).also {
                    INSTANCE = it
                }
            }
        }
    }

    private val sharedPreferences =
        context.getSharedPreferences("flow-mart-app", Context.MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    fun saveAPIKey(apiKey: String) {
        editor.putString("api_key", apiKey)
        editor.apply()
    }

    fun getAPIKey(): String? {
        return sharedPreferences.getString("api_key", "N/A")
    }

    fun saveUser(user: User) {
        editor.putInt("id", user.id)
        editor.putString("name", user.name)
        editor.putString("email", user.email)
        editor.putString("phone", user.phone)
        editor.apply()
    }

    fun getUser(): User {
        return User(
            id = sharedPreferences.getInt("id", 0),
            name = sharedPreferences.getString("name", "N/A")!!,
            email = sharedPreferences.getString("email", "N/A")!!,
            phone = sharedPreferences.getString("phone", "N/A")!!
        )
    }

    fun isLoggedIn(): Boolean {
        return getAPIKey() != "N/A"
    }
}