package com.orlinskas.notebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.ToastBuilder;
import com.orlinskas.notebook.fragment.DayFragment;
import com.orlinskas.notebook.fragment.DayFragmentActions;
import com.orlinskas.notebook.value.Day;

import org.parceler.Parcels;

import java.util.Objects;

import static com.orlinskas.notebook.Constants.AFTER_CREATE_OPEN_DAY;
import static com.orlinskas.notebook.Constants.IS_FULL_DISPLAY;
import static com.orlinskas.notebook.Constants.PARCEL_DAY;

public class ConcreteDayActivity extends AppCompatActivity implements DayFragmentActions {
    private ProgressBar progressBar;
    private Day day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_day);

        progressBar = findViewById(R.id.activity_concrete_day_pb);
        FloatingActionButton fab = findViewById(R.id.activity_concrete_day_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateNotificationActivity();
            }
        });

        if(savedInstanceState != null) {
            day = Parcels.unwrap(savedInstanceState.getParcelable(PARCEL_DAY));
        }
        else {
            day = Parcels.unwrap(Objects.requireNonNull(getIntent().getExtras()).getParcelable(PARCEL_DAY));
        }

        showDayNotifications();
    }

    private void showDayNotifications() {
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.activity_concrete_day_container);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(PARCEL_DAY, Parcels.wrap(day));
            bundle.putBoolean(IS_FULL_DISPLAY, true);

            fragment = new DayFragment();
            fragment.setArguments(bundle);

            fm.beginTransaction()
                    .add(R.id.activity_concrete_day_container, fragment)
                    .commit();
        }

    }

    public void openCreateNotificationActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setAction(AFTER_CREATE_OPEN_DAY);
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCEL_DAY, Parcels.wrap(day));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCEL_DAY, Parcels.wrap(day));
    }

    @Override
    public void openDay(Day day) {

    }

    @Override
    public void deleteNotification(int deletedNotificationID) {
        ToastBuilder.doToast(getApplicationContext(), "Delete...");
    }
}
