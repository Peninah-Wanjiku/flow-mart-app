package com.example.flowmart.data.api

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.flowmart.data.SharedPreferenceManager
import com.example.flowmart.utils.DialogUtil
import org.json.JSONObject

/**
 * @Author: Angatia Benson
 * @Date: 9/17/2024
 * Copyright (c) 2024 BanIT
 */

class APIClient private constructor(private val context: Context) {
    private val baseUrl = "https://flowmart.banit.co.ke/"

    /**
     * Companion object to ensure a single instance of the APIClient
     */
    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: APIClient? = null

        fun getInstance(context: Context): APIClient {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: APIClient(context).also { INSTANCE = it }
            }
        }
    }

    private val requestQueue: RequestQueue by lazy {
        // Initialize the RequestQueue with the application context
        Volley.newRequestQueue(context)
    }

    /**
     * Generic method to make HTTP requests.
     *
     * @param method HTTP method (Request.Method.GET, POST, PUT, etc.)
     * @param endpoint The endpoint URL
     * @param requestBody The JSON body for POST/PUT requests (null for GET)
     * @param successListener Callback for successful response
     * @param errorListener Callback for error response
     */
    private fun makeRequest(
        method: Int,
        endpoint: String,
        requestBody: JSONObject? = null,
        successListener: (JSONObject) -> Unit,
        errorListener: (JSONObject) -> Unit
    ) {
        val jsonObjectRequest = object : JsonObjectRequest(
            method,
            "$baseUrl$endpoint",
            requestBody,
            Response.Listener { response ->
                successListener(response)
            },
            Response.ErrorListener { error ->
                val responseBody = String(error.networkResponse.data, Charsets.UTF_8)
                val errorJson = JSONObject(responseBody)
                if (errorJson.getInt("code") == 401) {
                    if (context is Activity) {
                        context.runOnUiThread {
                            DialogUtil.showLoginResetDialog(context)
                        }
                        return@ErrorListener
                    }
                }
                errorListener(errorJson)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val apiKey = SharedPreferenceManager.getInstance(context).getAPIKey()
                val authorizationHeaderValue = "Bearer $apiKey"
                val mHeaders = mutableMapOf<String, String>()
                mHeaders["Authorization"] = authorizationHeaderValue
                mHeaders["Content-Type"] = "application/json"

                return mHeaders;
            }
        }

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest)
    }


    /**
     * Convenience method for GET requests.
     */
    fun get(
        endpoint: String,
        successListener: (JSONObject) -> Unit,
        errorListener: (JSONObject) -> Unit
    ) {
        makeRequest(
            method = Request.Method.GET,
            endpoint = endpoint,
            successListener = successListener,
            errorListener = errorListener
        )
    }

    /**
     * Convenience method for DELETE requests.
     */
    fun delete(
        endpoint: String,
        successListener: (JSONObject) -> Unit,
        errorListener: (JSONObject) -> Unit
    ) {
        makeRequest(
            method = Request.Method.DELETE,
            endpoint = endpoint,
            successListener = successListener,
            errorListener = errorListener
        )
    }

    /**
     * Convenience method for POST requests.
     */
    fun post(
        endpoint: String,
        requestBody: JSONObject,
        successListener: (JSONObject) -> Unit,
        errorListener: (JSONObject) -> Unit
    ) {
        makeRequest(
            method = Request.Method.POST,
            endpoint = endpoint,
            requestBody = requestBody,
            successListener = successListener,
            errorListener = errorListener
        )
    }

    /**
     * Convenience method for PUT requests.
     */
    fun put(
        endpoint: String,
        requestBody: JSONObject,
        successListener: (JSONObject) -> Unit,
        errorListener: (JSONObject) -> Unit
    ) {
        makeRequest(
            method = Request.Method.PUT,
            endpoint = endpoint,
            requestBody = requestBody,
            successListener = successListener,
            errorListener = errorListener
        )
    }
}
