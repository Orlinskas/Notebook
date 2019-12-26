package com.orlinskas.notebook.ui.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.orlinskas.notebook.Enums;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.builder.NotificationBuilder;
import com.orlinskas.notebook.builder.ToastBuilder;
import com.orlinskas.notebook.date.DateCurrent;
import com.orlinskas.notebook.date.DateFormater;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.ui.viewModel.MainViewModel;

import java.util.Calendar;
import java.util.Date;
import java.util.Objects;

public class CreateNotificationView extends AppCompatActivity {
    private ProgressBar progressBar;
    private EditText notificationBody;
    private TextView dateTimeTV;
    private Calendar dateTime;
    private RelativeLayout layout;
    private MainViewModel model;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_notification);
        dateTime = Calendar.getInstance();
        progressBar = findViewById(R.id.activity_create_notification_pb);
        notificationBody = findViewById(R.id.activity_create_notification_et_note);
        dateTimeTV = findViewById(R.id.activity_create_notification_tv_date_value);
        layout = findViewById(R.id.activity_create_notification_rl);
        final RelativeLayout relativeLayout = findViewById(R.id.activity_create_notification_rl_date_block);
        final Button createButton = findViewById(R.id.activity_create_notification_btn_create);
        final Animation clickAnimation = AnimationUtils.loadAnimation(this, R.anim.animation_button);

        relativeLayout.setOnClickListener(v -> {
            relativeLayout.startAnimation(clickAnimation);
            setDate();
        });

        createButton.setOnClickListener(v -> {
            createButton.startAnimation(clickAnimation);
            if(checkValidData()){
                createNotification();
            }
        });

        if (savedInstanceState != null) {
            dateTimeTV.setText(savedInstanceState.getString("date"));
        }

        model = ViewModelProviders.of(this).get(MainViewModel.class);

        model.getRepositoryStatusData().observe(this, repositoryStatusEnum -> {
            if (repositoryStatusEnum == Enums.RepositoryStatus.LOADING) {
                progressBar.setVisibility(View.VISIBLE);
            }
            if (repositoryStatusEnum == Enums.RepositoryStatus.READY) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void setDate() {
        new DatePickerDialog(CreateNotificationView.this, datePickerListener,
                dateTime.get(Calendar.YEAR),
                dateTime.get(Calendar.MONTH),
                dateTime.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    public void setTime() {
        new TimePickerDialog(CreateNotificationView.this, timePickerListener,
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
            ToastBuilder.doToast(this, "Empty note");
            return false;
        }
        if(notificationDate.before(DateCurrent.get())){
            ToastBuilder.doToast(this, "Not valid date, try again");
            return false;
        }
        if(dateTimeTV.getText().toString().equals("—")){
            ToastBuilder.doToast(this, "Not valid date, try again");
            return false;
        }

        return true;
    }

    private void createNotification() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if(imm != null) {
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }

        String bodyText = notificationBody.getText().toString();
        String dateTime = dateTimeTV.getText().toString();

        NotificationBuilder builder = new NotificationBuilder(getApplicationContext());
        Notification notification = builder.build(bodyText, dateTime);

        model.createNotification(notification);

        ToastBuilder.createSnackBar(layout, "Создается, можно создать новую");

        notificationBody.setText("");
        dateTimeTV.setText("—");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("date", dateTimeTV.getText().toString());
    }
}