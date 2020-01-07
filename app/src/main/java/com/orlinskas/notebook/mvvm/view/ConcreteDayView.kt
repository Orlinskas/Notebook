package com.orlinskas.notebook.mvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orlinskas.notebook.Constants.*
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.R
import com.orlinskas.notebook.builder.ToastBuilder
import com.orlinskas.notebook.mvvm.fragment.ConnectionStatusFragment
import com.orlinskas.notebook.mvvm.fragment.DayFragmentActions
import com.orlinskas.notebook.mvvm.fragment.DayView
import com.orlinskas.notebook.mvvm.model.Notification
import com.orlinskas.notebook.mvvm.viewModel.BaseViewModel
import com.orlinskas.notebook.mvvm.viewModel.ConcreteDayViewModel
import kotlinx.android.synthetic.main.activity_concrete_day.*

class ConcreteDayView : AppCompatActivity(), DayFragmentActions {
    private lateinit var progressBar: ProgressBar
    private var dayID: Int = 0
    private lateinit var model: BaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_concrete_day)

        progressBar = findViewById(R.id.activity_concrete_day_pb)
        activity_concrete_day_fab.setOnClickListener {
            openCreateNotificationActivity()
        }

        model = ViewModelProviders.of(this).get(BaseViewModel::class.java)

        model.daysData.observe(this, Observer {
            updateUI()
        })

        model.downloadStatusData.observe(this, Observer {
            when (it) {
                Enums.DownloadStatus.LOADING -> progressBar.visibility = View.VISIBLE
                Enums.DownloadStatus.READY -> progressBar.visibility = View.INVISIBLE
            }
        })

        if (savedInstanceState != null) {
            dayID = savedInstanceState.getInt(DAY_ID)
        }
        else {
            intent.extras?.let {
                dayID = it.getInt(DAY_ID)
            }
        }

        addFragmentConnectionStatus()
    }

    private fun addFragmentConnectionStatus() {
        val fm = supportFragmentManager

        var fragment = fm.findFragmentById(R.id.activity_concrete_day_connection_container)
        if (fragment == null) {
            fragment = ConnectionStatusFragment()
            fm.beginTransaction()
                    .add(R.id.activity_concrete_day_connection_container, fragment)
                    .commit()
        }
    }

    private fun updateUI() {
        val fm = supportFragmentManager

        var fragment = fm.findFragmentById(R.id.activity_concrete_day_container)
        if (fragment == null) {
            val bundle = Bundle()
            bundle.putInt(DAY_ID, dayID)
            bundle.putBoolean(IS_FULL_DISPLAY, true)

            fragment = DayView()
            fragment.arguments = bundle

            fm.beginTransaction()
                    .add(R.id.activity_concrete_day_container, fragment)
                    .commit()
        } else {
            fm.beginTransaction().detach(fragment).attach(fragment).commit()
        }
    }

    private fun doToast(message: String) {
        if (!isFinishing) {
            runOnUiThread { ToastBuilder.doToast(application.applicationContext, message) }
        }
    }

    private fun openCreateNotificationActivity() {
        val intent = Intent(applicationContext, CreateNotificationView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        intent.action = AFTER_CREATE_OPEN_DAY
        startActivity(intent)
    }

    override fun openDay(dayID: Int) {
        //not specified in the technical specifications for this window
    }

    override fun deleteNotification(notification: Notification) {
        model.deleteNotification(notification)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(DAY_ID, dayID)
    }
}