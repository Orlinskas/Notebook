package com.orlinskas.notebook

import android.util.Log

class RemoteServiceException : Exception() {

    override fun printStackTrace() {
        super.printStackTrace()
        Log.e("RemoteServiceException", super.message.toString() )
    }

    override val message: String?
        get() = super.message
}