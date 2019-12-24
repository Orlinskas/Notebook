package com.orlinskas.notebook.activity

interface ViewModel {
    suspend fun updateUI()
    suspend fun failConnection()
    suspend fun doneConnection()
}