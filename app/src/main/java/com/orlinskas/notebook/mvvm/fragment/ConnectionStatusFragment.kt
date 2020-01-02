package com.orlinskas.notebook.mvvm.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.orlinskas.notebook.App.Component.app
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.R
import com.orlinskas.notebook.visual.IconTinter
import javax.inject.Inject

class ConnectionStatusFragment : Fragment() {
    @Inject lateinit var connectionStatusData: MutableLiveData<Enum<Enums.ConnectionStatus>>

    init {
        app.getComponent().inject(this)
    }

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