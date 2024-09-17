package com.example.flowmart.utils

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.flowmart.R

/**
 * @Author: Angatia Benson
 * @Date: 9/17/2024
 * Copyright (c) 2024 BanIT
 */

object LoadingDialog {
    private var dialog: AlertDialog? = null

    /**
     * Shows the loading dialog.
     *
     * @param context The context to use. Usually the Activity.
     * @param message Optional message to display. Defaults to "Loading...".
     */
    @SuppressLint("MissingInflatedId")
    fun show(context: Context, message: String? = "Loading...") {
        if (dialog?.isShowing == true) {
            // Dialog is already showing
            return
        }

        val builder = AlertDialog.Builder(context)
        // Inflate the custom layout
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.dialog_loading, null)

        // Set the message if provided
        val tvLoading = view.findViewById<TextView>(R.id.tvLoading)
        message?.let {
            tvLoading.text = it
        }

        builder.setView(view)
        builder.setCancelable(false) // Prevent dialog from being canceled

        dialog = builder.create()
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.show()
    }

    /**
     * Hides the loading dialog if it's showing.
     */
    fun hide() {
        dialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        dialog = null
    }
}
