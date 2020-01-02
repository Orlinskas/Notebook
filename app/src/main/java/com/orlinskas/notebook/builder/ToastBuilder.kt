package com.orlinskas.notebook.builder

import android.content.Context
import android.view.View
import android.widget.Toast

import com.google.android.material.snackbar.Snackbar

object ToastBuilder {

    fun doToast(context: Context, message: String) {
        val toast = Toast.makeText(context,
                message, Toast.LENGTH_SHORT)
        toast.show()
    }

    fun createSnackBar(view: View, message: String) {
        try {
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
}
