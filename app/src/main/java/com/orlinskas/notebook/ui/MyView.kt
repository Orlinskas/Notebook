package com.orlinskas.notebook.ui

interface MyView {
    fun updateUI()
    fun startProgress()
    fun stopProgress()
    fun showToast(message: String)
    fun showSnackBar(message: String)
    fun openPrevWindow()
}