package com.orlinskas.notebook.activity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.entity.Notification;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.orlinskas.notebook.R;
import com.orlinskas.notebook.builder.NotificationBuilder;
import com.orlinskas.notebook.date.DateFormater;
import com.orlinskas.notebook.date.DateCurrent;

import java.util.Calendar;
import java.util.Date;

public class CreateNotificationActivity extends AppCompatActivity {
    ProgressBar progressBar;
    EditText notificationBody;
    TextView dateTimeTV;
    Calendar dateTime;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
        dateTime = Calendar.getInstance();
        progressBar = findViewById(R.id.activity_create_notification_pb);
        notificationBody = findViewById(R.id.activity_create_notification_et_note);
        dateTimeTV = findViewById(R.id.activity_create_notification_tv_date_value);
        final RelativeLayout relativeLayout = findViewById(R.id.activity_create_notification_rl_date_block);
        final Button createButton = findViewById(R.id.activity_create_notification_btn_create);

        final Animation clickAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_button);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                relativeLayout.startAnimation(clickAnimation);
                setDate();
            }
        });

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createButton.startAnimation(clickAnimation);
                if(checkValidData()){
                    new CreateNotificationTask().execute();
                }
            }
        });
    }

    public void setDate() {
        new DatePickerDialog(CreateNotificationActivity.this, datePickerListener,
                dateTime.get(Calendar.YEAR),
                dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setTime() {
        new TimePickerDialog(CreateNotificationActivity.this, timePickerListener,
                dateTime.get(Calendar.HOUR_OF_DAY),
                dateTime.get(Calendar.MINUTE), true)
                .show();
    }

    DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateTime.set(Calendar.YEAR, year);
            dateTime.set(Calendar.MONTH, monthOfYear);
            dateTime.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            setTime();
        }
    };

    TimePickerDialog.OnTimeSetListener timePickerListener = new TimePickerDialog.OnTimeSetListener() {
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            dateTime.set(Calendar.HOUR_OF_DAY, hourOfDay);
            dateTime.set(Calendar.MINUTE, minute);
            setInitialDateTime();
        }
    };

    private void setInitialDateTime() {
        Date notificationDate = new Date(dateTime.getTimeInMillis());
        dateTimeTV.setText(DateFormater.format(notificationDate, DateFormater.YYYY_MM_DD_HH_MM));
    }

    private boolean checkValidData() {
        Date notificationDate =
                DateFormater.format(dateTimeTV.getText().toString(), DateFormater.YYYY_MM_DD_HH_MM);

        if(notificationBody.getText().length() < 1) {
            doToast("Empty note");
            return false;
        }
        if(notificationDate.before(DateCurrent.get())){
            doToast("Not valid date, try again");
            return false;
        }
        if(dateTimeTV.getText().toString().equals("—")){
            doToast("Not valid date, try again");
            return false;
        }

        return true;
    }

    private void doToast(String message) {
        Toast toast = Toast.makeText(getApplicationContext(),
                message, Toast.LENGTH_LONG);
        toast.show();
    }

    @SuppressLint("StaticFieldLeak")
    private class CreateNotificationTask extends AsyncTask<Void, Void, Void> {
        private String bodyText;
        private String dateTime;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            bodyText = notificationBody.getText().toString();
            dateTime = dateTimeTV.getText().toString();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            NotificationBuilder builder = new NotificationBuilder();
            Notification notification = builder.build(bodyText, dateTime);
            MyDatabase database = App.getInstance().getMyDatabase();
            database.notifiationDao().insertAll(notification);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(View.INVISIBLE);
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Done", Toast.LENGTH_LONG);
            toast.show();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
    }
}