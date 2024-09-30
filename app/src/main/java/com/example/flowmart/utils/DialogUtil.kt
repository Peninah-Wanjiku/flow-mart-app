package com.example.flowmart.utils

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.example.flowmart.data.SharedPreferenceManager
import com.jakewharton.processphoenix.ProcessPhoenix

/**
 * @Author: Angatia Benson
 * @Date: 9/30/2024
 * Copyright (c) 2024 BanIT
 */
object DialogUtil {
    fun showLoginResetDialog(context: Context) {
        // Build the dialog
        val dialogBuilder = AlertDialog.Builder(context)
            .setTitle("Session Expired")
            .setMessage("Your key has been reset due to another login. Please log in again.")
            .setCancelable(false)
            .setPositiveButton("Login") { _, _ ->
                SharedPreferenceManager.getInstance(context).clear()
                ProcessPhoenix.triggerRebirth(context);
            }

        // Show the dialog
        val dialog = dialogBuilder.create()
        dialog.show()
    }
}