package com.dogshare.ui.components

import android.content.Context
import android.widget.Toast

object ToastUtils {
    // Function to show a toast with a default short duration
    fun showToast(context: Context, message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    // Function to show a toast with a custom duration (either Toast.LENGTH_SHORT or Toast.LENGTH_LONG)
    fun showToast(context: Context, message: String, duration: Int) {
        Toast.makeText(context, message, duration).show()
    }
}
