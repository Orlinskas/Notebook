package com.orlinskas.notebook

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import javax.inject.Inject

class NetworkHandler @Inject constructor(private val context: Context) {
    val isConnected
        get() : Boolean {
            val networkInfo = context.networkInfo
            return networkInfo?.isConnected ?: false
        }
}

val Context.networkInfo: NetworkInfo? get() =
    (this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo