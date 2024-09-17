package com.example.flowmart.api

import android.annotation.SuppressLint
import android.content.Context
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

/**
 * @Author: Angatia Benson
 * @Date: 9/17/2024
 * Copyright (c) 2024 BanIT
 */

class APIClient private constructor(private val context: Context) {

    companion object {
        @SuppressLint("StaticFieldLeak")
        @Volatile
        private var INSTANCE: APIClient? = null

        fun getInstance(context: Context): APIClient {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: APIClient(context.applicationContext).also { INSTANCE = it }
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
     * @param url The endpoint URL
     * @param jsonRequest The JSON body for POST/PUT requests (null for GET)
     * @param successListener Callback for successful response
     * @param errorListener Callback for error response
     */
    private fun makeRequest(
        method: Int,
        url: String,
        jsonRequest: JSONObject? = null,
        headers: Map<String, String>? = null,
        successListener: (JSONObject) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        val jsonObjectRequest = object : JsonObjectRequest(
            method,
            url,
            jsonRequest,
            Response.Listener { response ->
                successListener(response)
            },
            Response.ErrorListener { error ->
                errorListener(error)
            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                // Combine default headers with custom headers
                val defaultHeaders = super.getHeaders()
                val customHeaders = headers ?: emptyMap()
                return defaultHeaders + customHeaders
            }
        }

        // Add the request to the RequestQueue
        requestQueue.add(jsonObjectRequest)
    }


    /**
     * Convenience method for GET requests.
     */
    fun get(
        url: String,
        headers: Map<String, String>? = null,
        successListener: (JSONObject) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        makeRequest(
            method = Request.Method.GET,
            url = url,
            headers = headers,
            successListener = successListener,
            errorListener = errorListener
        )
    }

    /**
     * Convenience method for DELETE requests.
     */
    fun delete(
        url: String,
        headers: Map<String, String>? = null,
        successListener: (JSONObject) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        makeRequest(
            method = Request.Method.DELETE,
            url = url,
            headers = headers,
            successListener = successListener,
            errorListener = errorListener
        )
    }

    /**
     * Convenience method for POST requests.
     */
    fun post(
        url: String,
        headers: Map<String, String>? = null,
        jsonRequest: JSONObject,
        successListener: (JSONObject) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        makeRequest(
            method = Request.Method.POST,
            url = url,
            headers = headers,
            jsonRequest = jsonRequest,
            successListener = successListener,
            errorListener = errorListener
        )
    }

    /**
     * Convenience method for PUT requests.
     */
    fun put(
        url: String,
        headers: Map<String, String>? = null,
        jsonRequest: JSONObject,
        successListener: (JSONObject) -> Unit,
        errorListener: (Exception) -> Unit
    ) {
        makeRequest(
            method = Request.Method.PUT,
            url = url,
            headers = headers,
            jsonRequest = jsonRequest,
            successListener = successListener,
            errorListener = errorListener
        )
    }
}
