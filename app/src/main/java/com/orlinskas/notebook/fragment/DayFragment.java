package com.orlinskas.notebook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.notificationHelper.NotificationListAdapter;
import com.orlinskas.notebook.value.Day;

import java.util.List;
import java.util.Objects;

import static com.orlinskas.notebook.Constants.COUNT_NOTIFICATION_IN_SHORT_LIST;
import static com.orlinskas.notebook.Constants.DAY_ID;
import static com.orlinskas.notebook.Constants.IS_FULL_DISPLAY;

public class DayFragment extends Fragment {
    private int dayID;
    private LiveData<List<Day>> daysData;
    private DayFragmentActions fragmentActions;
    private Context context;
    private boolean isFullDisplay;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        fragmentActions = (DayFragmentActions) context;
        daysData = App.getInstance().getDaysLiveData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        if(savedInstanceState != null) {
            dayID = savedInstanceState.getInt(DAY_ID);
        }
        else {
            if (getArguments() != null) {
                dayID = getArguments().getInt(DAY_ID);
                isFullDisplay = getArguments().getBoolean(IS_FULL_DISPLAY);
            }
        }

        Day day = daysData.getValue().get(dayID);

        TextView dayName = view.findViewById(R.id.fragment_notification_tv_day_name);
        TextView dayDate = view.findViewById(R.id.fragment_notification_tv_day_date);
        ListView notificationList = view.findViewById(R.id.fragment_notification_ll_notification_container);

        dayName.setText(day.getDayName());
        dayDate.setText(day.getDayDate());

        ArrayAdapter adapter;

        if(isFullDisplay){
            adapter = new NotificationListAdapter(context, R.layout.item_notification_full, day.getNotifications());
        }
        else {
            adapter = new NotificationListAdapter(context, R.layout.item_notification_small, day.getNotifications());
        }

        notificationList.setAdapter(adapter);
        setListViewHeightBasedOnChildren(notificationList);

        final Animation clickAnimation = AnimationUtils.loadAnimation(context, R.anim.animation_button);

        view.setOnClickListener(v -> {
            v.startAnimation(clickAnimation);
            fragmentActions.openDay(dayID);
        });
        notificationList.setOnItemClickListener((parent, view1, position, id) -> {
            view1.startAnimation(clickAnimation);
            fragmentActions.openDay(dayID);
        });

        if(isFullDisplay){
            notificationList.setOnItemLongClickListener((parent, view12, position, id) -> {
                FragmentManager manager = getFragmentManager();
                DeleteDialogFragment deleteDialogFragment
                        = new DeleteDialogFragment(day.getNotifications().get(position));
                deleteDialogFragment.show(Objects.requireNonNull(manager), "dialog");
                return true;
            });
        }

        return view;
    }

    private void setListViewHeightBasedOnChildren(ListView listView) {
        ArrayAdapter listAdapter = (ArrayAdapter) listView.getAdapter();

        int totalHeight = 0;
        int maxCount;

        if(listAdapter.getCount() >= COUNT_NOTIFICATION_IN_SHORT_LIST && !isFullDisplay){
            maxCount = COUNT_NOTIFICATION_IN_SHORT_LIST;
        }
        else {
            maxCount = listAdapter.getCount();
        }

        for (int i = 0; i < maxCount; i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DAY_ID, dayID);
    }
}