package com.orlinskas.notebook.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.orlinskas.notebook.R;
import com.orlinskas.notebook.date.DateFormater;
import com.orlinskas.notebook.entity.Notification;

import java.util.Date;
import java.util.List;

public class NotificationListAdapter extends ArrayAdapter<Notification> {
    private Context context;
    private int item_id;
    private List<Notification> notifications;


    NotificationListAdapter(Context context, int item_id, List<Notification> notifications) {
        super(context, item_id, notifications);
        this.context = context;
        this.item_id = item_id;
        this.notifications = notifications;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        @SuppressLint("ViewHolder")
        View row = inflater.inflate(item_id, parent, false);

        TextView bodyTV;
        TextView timeTV;

        switch (item_id) {
            default:
            case R.layout.item_notification_small:
                bodyTV = row.findViewById(R.id.item_notification_small_tv_body);
                timeTV = row.findViewById(R.id.item_notification_small_tv_time);
                break;
            case R.layout.item_notification_full:
                bodyTV = row.findViewById(R.id.item_notification_full_tv_body);
                timeTV = row.findViewById(R.id.item_notification_full_tv_time);
                break;
        }

        String body = "default";
        String time = "default";
        try {
            body = notifications.get(position).getBodyText();
            Date notificationDate = new Date(notifications.get(position).getStartDateMillis());
            time = DateFormater.format(notificationDate, DateFormater.HH_MM);
        } catch (Exception e) {
            e.printStackTrace();
        }

        bodyTV.setText(body);
        timeTV.setText(time);

        return row;
    }
}


