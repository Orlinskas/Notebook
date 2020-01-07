package com.orlinskas.notebook.mvvm.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.orlinskas.notebook.Constants.*
import com.orlinskas.notebook.R
import com.orlinskas.notebook.mvvm.model.Day
import com.orlinskas.notebook.notificationHelper.NotificationListAdapter
import kotlinx.android.synthetic.main.fragment_day.*
import java.util.*

class DayView : Fragment() {
    private var dayID: Int = 0
    private var isFullDisplay: Boolean = false
    private lateinit var fragmentActions: DayFragmentActions
    private lateinit var model: DayViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentActions = context as DayFragmentActions
        model = ViewModelProviders.of(this).get(DayViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_day, container, false)

        if (savedInstanceState != null) {
            dayID = savedInstanceState.getInt(DAY_ID)
        } else {
            arguments?.let {
                dayID = it.getInt(DAY_ID)
                isFullDisplay = it.getBoolean(IS_FULL_DISPLAY)
            }
        }

        model.daysData.observe(this, Observer {
            updateUI(it[dayID], view)
        })

        return view
    }

    private fun updateUI(day: Day, view: View) {
        val notificationList = fragment_notification_ll_notification_container

        fragment_notification_tv_day_name.text = day.dayName
        fragment_notification_tv_day_date.text = day.dayDate

        val adapter: ArrayAdapter<*> = if (isFullDisplay) {
            NotificationListAdapter(context, R.layout.item_notification_full, day.notifications)
        } else {
            NotificationListAdapter(context, R.layout.item_notification_small, day.notifications)
        }

        notificationList.adapter = adapter
        setListViewHeightBasedOnChildren(notificationList)

        val clickAnimation = AnimationUtils.loadAnimation(context, R.anim.animation_button)

        view.setOnClickListener {
            it.startAnimation(clickAnimation)
            fragmentActions.openDay(dayID)
        }

        notificationList.setOnItemClickListener { _, _, _, _ ->
            notificationList.startAnimation(clickAnimation)
            fragmentActions.openDay(dayID)
        }

        if (isFullDisplay) {
            notificationList.setOnItemLongClickListener { _, _, position, _ ->
                val manager = fragmentManager
                val deleteDialogFragment = DeleteDialogFragment(day.notifications[position])
                deleteDialogFragment.show(Objects.requireNonNull<FragmentManager>(manager), "dialog")
                true
            }
        }
    }

    private fun setListViewHeightBasedOnChildren(listView: ListView) {
        val listAdapter = listView.adapter as ArrayAdapter<*>

        var totalHeight = 0
        val maxCount: Int

        maxCount = if (listAdapter.count >= COUNT_NOTIFICATION_IN_SHORT_LIST && !isFullDisplay) {
            COUNT_NOTIFICATION_IN_SHORT_LIST
        } else {
            listAdapter.count
        }

        for (i in 0 until maxCount) {
            val listItem = listAdapter.getView(i, null, listView)
            listItem.measure(0, 0)
            totalHeight += listItem.measuredHeight
        }

        val params = listView.layoutParams
        params.height = totalHeight + listView.dividerHeight * (listAdapter.count - 1)
        listView.layoutParams = params
        listView.requestLayout()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(DAY_ID, dayID)
    }
}