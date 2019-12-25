package com.orlinskas.notebook.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;

import com.orlinskas.notebook.CoroutinesFunKt;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.builder.NotificationBuilder;
import com.orlinskas.notebook.builder.ToastBuilder;
import com.orlinskas.notebook.date.DateCurrent;
import com.orlinskas.notebook.date.DateFormater;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.repository.NotificationRepository;
import com.orlinskas.notebook.value.Day;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Job;

public class CreateNotificationActivity extends AppCompatActivity implements ConnectionCallBack{
    private ProgressBar progressBar;
    private EditText notificationBody;
    private TextView dateTimeTV;
    private Calendar dateTime;
    private Job job = null;
    private CoroutineScope scope = CoroutinesFunKt.getIoScope();

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
        progressBar.setVisibility(View.VISIBLE);
        String bodyText = notificationBody.getText().toString();
        String dateTime = dateTimeTV.getText().toString();

        NotificationBuilder builder = new NotificationBuilder(getApplicationContext());
        Notification notification = builder.build(bodyText, dateTime);

        NotificationRepository repository = new NotificationRepository(this);

        job = BuildersKt.launch(scope, scope.getCoroutineContext(), CoroutineStart.DEFAULT,
                (scope, continuation) -> { repository.insert(notification, new Continuation<MutableLiveData<List<Day>>>() {
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return scope.getCoroutineContext();
                    }
                    @Override
                    public void resumeWith(@NotNull Object o) {
                        openPreviousActivity();
                    }
                });
                return scope.getCoroutineContext();
        });
    }

    @Override
    public Object failConnection(@NotNull Continuation<? super Unit> continuation) {
        return null;
    }

    @Override
    public Object doneConnection(@NotNull Continuation<? super Unit> continuation) {
        return null;
    }

    private void openPreviousActivity() {
        runOnUiThread(() -> {
            progressBar.setVisibility(View.INVISIBLE);
            ToastBuilder.doToast(getApplicationContext(), "Done");
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("date", dateTimeTV.getText().toString());
    }
}