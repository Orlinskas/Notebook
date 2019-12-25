package com.orlinskas.notebook.activity

interface ConnectionCallBack {
    suspend fun failConnection()
    suspend fun doneConnection()
}