package com.orlinskas.notebook.mvvm.view

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orlinskas.notebook.Enums
import com.orlinskas.notebook.R
import com.orlinskas.notebook.builder.NotificationBuilder
import com.orlinskas.notebook.builder.ToastBuilder
import com.orlinskas.notebook.date.DateCurrent
import com.orlinskas.notebook.date.DateFormater
import com.orlinskas.notebook.mvvm.fragment.ConnectionStatusFragment
import com.orlinskas.notebook.mvvm.viewModel.CreateNotificationModel
import kotlinx.android.synthetic.main.activity_create_notification.*
import java.util.*

class CreateNotificationView : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private lateinit var notificationBody: EditText
    private lateinit var dateTimeTV: TextView
    private lateinit var dateTime: Calendar
    private lateinit var layout: RelativeLayout
    private lateinit var model: CreateNotificationModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_notification)
        dateTime = Calendar.getInstance()
        progressBar = activity_create_notification_pb
        notificationBody = activity_create_notification_et_note
        dateTimeTV = activity_create_notification_tv_date_value
        layout = activity_create_notification_rl
        val clickAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_button)

        if (savedInstanceState != null) {
            dateTimeTV.text = savedInstanceState.getString("date")
        }

        activity_create_notification_rl_date_block.setOnClickListener { v ->
            v.startAnimation(clickAnimation)
            setDate()
        }

        activity_create_notification_btn_create.setOnClickListener { v ->
            v.startAnimation(clickAnimation)
            if (checkValidData()) {
                createNotification()
            }
        }

        model = ViewModelProviders.of(this).get(CreateNotificationModel::class.java)

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

    private fun addFragmentConnectionStatus() {
        val fm = supportFragmentManager

        var fragment = fm.findFragmentById(R.id.activity_create_notification_fragment_container)
        if (fragment == null) {
            fragment = ConnectionStatusFragment()
            fm.beginTransaction()
                    .add(R.id.activity_create_notification_fragment_container, fragment)
                    .commit()
        }
    }

    private fun setDate() {
        val datePickerListener: DatePickerDialog.OnDateSetListener =
                DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                    dateTime.set(Calendar.YEAR, year)
                    dateTime.set(Calendar.MONTH, monthOfYear)
                    dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    setTime()
                }

        DatePickerDialog(this@CreateNotificationView, datePickerListener,
                dateTime.get(Calendar.YEAR),
                dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH))
                .show()
    }

    private fun setTime() {
        val timePickerListener: TimePickerDialog.OnTimeSetListener =
                TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
                    dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
                    dateTime.set(Calendar.MINUTE, minute)
                    setInitialDateTime()
                }

        TimePickerDialog(this@CreateNotificationView, timePickerListener,
                dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE), true)
                .show()
    }

    private fun setInitialDateTime() {
        val notificationDate = Date(dateTime.timeInMillis)
        dateTimeTV.text = DateFormater.format(notificationDate, DateFormater.YYYY_MM_DD_HH_MM)
    }

    private fun checkValidData(): Boolean {
        val notificationDate = DateFormater.format(dateTimeTV.text.toString(), DateFormater.YYYY_MM_DD_HH_MM)

        if (notificationBody.text.isEmpty()) {
            doToast("Empty note")
            return false
        }
        if (notificationDate.before(DateCurrent.get())) {
            doToast("Not valid date, try again")
            return false
        }
        if (dateTimeTV.text.toString() == "—") {
            doToast("Not valid date, try again")
            return false
        }

        return true
    }

    private fun createNotification() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(Objects.requireNonNull<View>(currentFocus).windowToken, InputMethodManager.HIDE_NOT_ALWAYS)

        val bodyText = notificationBody.text.toString()
        val dateTime = dateTimeTV.text.toString()

        val builder = NotificationBuilder(applicationContext)
        val notification = builder.build(bodyText, dateTime)

        model.createNotification(notification)

        ToastBuilder.createSnackBar(layout, "Создается, можно создать новую")

        notificationBody.setText("")
        dateTimeTV.text = "—"
    }

    private fun doToast(message: String) {
        if (!isFinishing) {
            runOnUiThread { ToastBuilder.doToast(application.applicationContext, message) }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString("date", dateTimeTV.text.toString())
    }
}