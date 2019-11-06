package com.orlinskas.notebook.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.orlinskas.notebook.R;
import com.orlinskas.notebook.entity.Day;

import org.parceler.Parcels;

import static com.orlinskas.notebook.ParcelConstants.PARCEL_DAY;

public class DayFragment extends Fragment {
    private Day day;
    private DayFragmentActions fragmentActions;
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        fragmentActions = (DayFragmentActions) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        if(savedInstanceState != null) {
            day = Parcels.unwrap(savedInstanceState.getParcelable(PARCEL_DAY));
        }
        else {
            if (getArguments() != null) {
                day = Parcels.unwrap(getArguments().getParcelable(PARCEL_DAY));
            }
        }

        TextView dayName = view.findViewById(R.id.fragment_notification_tv_day_name);
        TextView dayDate = view.findViewById(R.id.fragment_notification_tv_day_date);
        ListView notificationList = view.findViewById(R.id.fragment_notification_lv_notifications);

        dayName.setText(day.getDayName());
        dayDate.setText(day.getDayDate());

        if(day.getNotifications() != null) {
            ArrayAdapter adapter = new NotificationListAdapter(context, R.layout.item_notification_small, day.getNotifications());
            notificationList.setAdapter(adapter);
        }

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentActions.openDay(day);
            }
        });

        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCEL_DAY, Parcels.wrap(day));
    }
}
