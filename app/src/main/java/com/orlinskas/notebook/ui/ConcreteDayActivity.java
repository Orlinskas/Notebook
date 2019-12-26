package com.orlinskas.notebook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orlinskas.notebook.App;
import com.orlinskas.notebook.CoroutinesFunKt;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.builder.ToastBuilder;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.fragment.DayFragment;
import com.orlinskas.notebook.fragment.DayFragmentActions;
import com.orlinskas.notebook.repository.NotificationRepository;
import com.orlinskas.notebook.value.Day;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;

import static com.orlinskas.notebook.Constants.AFTER_CREATE_OPEN_DAY;
import static com.orlinskas.notebook.Constants.DAY_ID;
import static com.orlinskas.notebook.Constants.IS_FULL_DISPLAY;

public class ConcreteDayActivity extends AppCompatActivity implements DayFragmentActions {
    private ProgressBar progressBar;
    private LiveData<List<Day>> daysData;
    private int dayID;
    private Job job = null;
    private CoroutineScope scope = CoroutinesFunKt.getMainScope();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_day);

        progressBar = findViewById(R.id.activity_concrete_day_pb);
        FloatingActionButton fab = findViewById(R.id.activity_concrete_day_fab);
        fab.setOnClickListener(view -> openCreateNotificationActivity());

        daysData = App.getInstance().getDaysLiveData();
        daysData.observe(this, days -> {
            ConcreteDayActivity.this.showDayNotifications();
        });

        if(savedInstanceState != null) {
            dayID = savedInstanceState.getInt(DAY_ID);
        }
        else {
            dayID = Objects.requireNonNull(getIntent().getExtras()).getInt("dayID");
        }

        showDayNotifications();
    }

    private void showDayNotifications() {
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.activity_concrete_day_container);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(DAY_ID, dayID);
            bundle.putBoolean(IS_FULL_DISPLAY, true);

            fragment = new DayFragment();
            fragment.setArguments(bundle);

            fm.beginTransaction()
                    .add(R.id.activity_concrete_day_container, fragment)
                    .commit();
        } else {
            fm.beginTransaction().detach(fragment).attach(fragment).commit();
        }
    }

    public void openCreateNotificationActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setAction(AFTER_CREATE_OPEN_DAY);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DAY_ID, dayID);
    }

    @Override
    public void openDay(int dayID) {
        //not specified in the technical specifications for this window
    }

    @Override
    public void deleteNotification(Notification notification) {
        progressBar.setVisibility(View.VISIBLE);

        NotificationRepository repository = new NotificationRepository();

        job = BuildersKt.launch(Dispatchers::getIO, Dispatchers.getIO(), CoroutineStart.DEFAULT,
                (scope, coroutine) -> repository.delete(notification, new Continuation<Unit>() {
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return Dispatchers.getIO();
                    }
                    @Override
                    public void resumeWith(@NotNull Object o) {
                        try {
                            runOnUiThread(() -> progressBar.setVisibility(View.INVISIBLE));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(job != null) {
            job.cancel(new CancellationException());
        }
    }
}