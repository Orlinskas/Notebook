package com.orlinskas.notebook.mVVM.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.orlinskas.notebook.App
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.R
import com.orlinskas.notebook.visual.IconTinter

class ConnectionStatusFragment : Fragment() {
    private val repository = App.getInstance().repository
    private val connectionStatusData: LiveData<Enum<Enums.ConnectionStatus>> = repository.connectionStatusData

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_connection_status, container, false)

        val image = view.findViewById<ImageView>(R.id.fragment_internet_connection_iv)

        connectionStatusData.observe(this, Observer {
            when (it) {
                Enums.ConnectionStatus.CONNECTION_DONE -> setDoneImage(image)
                Enums.ConnectionStatus.CONNECTION_FAIL -> setFailImage(image)
            }
        })


        return view
    }

    private fun setDoneImage(imageView: ImageView) {
        if (!activity?.isFinishing!!) {
            IconTinter.tintImageView(imageView, R.color.holo_green_dark)
        }
    }

    private fun setFailImage(imageView: ImageView) {
        if (!activity?.isFinishing!!) {
            IconTinter.tintImageView(imageView, R.color.holo_red_light)
        }
    }
}