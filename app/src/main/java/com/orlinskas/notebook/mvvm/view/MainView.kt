package com.orlinskas.notebook.mvvm.view

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orlinskas.notebook.Constants.DAY_ID
import com.orlinskas.notebook.Constants.IS_FULL_DISPLAY
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.R
import com.orlinskas.notebook.builder.ToastBuilder
import com.orlinskas.notebook.mvvm.fragment.ConnectionStatusFragment
import com.orlinskas.notebook.mvvm.fragment.DayFragmentActions
import com.orlinskas.notebook.mvvm.fragment.DayView
import com.orlinskas.notebook.mvvm.model.Notification
import com.orlinskas.notebook.mvvm.viewModel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class MainView : AppCompatActivity(), DayFragmentActions {
    private lateinit var progressBar: ProgressBar
    private lateinit var model: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = activity_main_pb
        activity_main_fab.setOnClickListener {
            openCreateNotificationActivity()
        }

        model = ViewModelProviders.of(this).get(MainViewModel::class.java)

        model.daysData.observe(this, Observer {
            updateUI()
        })

        model.repositoryStatusData.observe(this, Observer {
            when (it) {
                Enums.RepositoryStatus.LOADING -> progressBar.visibility = View.VISIBLE
                Enums.RepositoryStatus.READY -> progressBar.visibility = View.INVISIBLE
            }
        })

        model.connectionStatusData.observe(this, Observer {
            //when (it) {
            //    Enums.ConnectionStatus.CONNECTION_DONE -> doToast(Constants.REMOTE)
            //    Enums.ConnectionStatus.CONNECTION_FAIL -> doToast(Constants.LOCAL)
            //}
        })

        addFragmentConnectionStatus()
    }

    private fun openCreateNotificationActivity() {
        val intent = Intent(applicationContext, CreateNotificationView::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        startActivity(intent)
    }

    private fun addFragmentConnectionStatus() {
        val fm = supportFragmentManager

        var fragment = fm.findFragmentById(R.id.activity_main_fragment_container)
        if (fragment == null) {
            fragment = ConnectionStatusFragment()
            fm.beginTransaction()
                    .add(R.id.activity_main_fragment_container, fragment)
                    .commit()
        }
    }

    private fun updateUI() {
        val days = model.daysData.value

        val fm = supportFragmentManager

        if (days != null) {
            for (index in days.indices) {
                var fragment = fm.findFragmentById(R.id.activity_main_ll_days_container)
                if (fragment == null) {
                    val bundle = Bundle()
                    bundle.putInt(DAY_ID, index)

                    fragment = DayView()
                    fragment.arguments = bundle

                    fm.beginTransaction()
                            .add(R.id.activity_main_ll_days_container, fragment)
                            .commit()
                } else {
                    fm.beginTransaction().detach(fragment).attach(fragment).commit()
                }
            }
        }

    }

    private fun doToast(message: String) {
        if (!isFinishing) {
            runOnUiThread { ToastBuilder.doToast(application.applicationContext, message) }
        }
    }

    override fun openDay(dayID: Int) {
        val bundle = Bundle()
        bundle.putInt(DAY_ID, dayID)
        bundle.putBoolean(IS_FULL_DISPLAY, true)
        val intent = Intent(baseContext, ConcreteDayView::class.java)
        intent.putExtras(bundle)
        startActivity(intent)
    }

    override fun deleteNotification(notification: Notification) {
        //not specified in the technical specifications for this window
    }
}
