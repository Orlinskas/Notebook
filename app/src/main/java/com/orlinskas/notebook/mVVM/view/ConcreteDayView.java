package com.orlinskas.notebook.mVVM.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orlinskas.notebook.Constants;
import com.orlinskas.notebook.Enums;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.builder.ToastBuilder;
import com.orlinskas.notebook.mVVM.model.Notification;
import com.orlinskas.notebook.mVVM.fragment.DayView;
import com.orlinskas.notebook.mVVM.fragment.DayFragmentActions;
import com.orlinskas.notebook.mVVM.viewModel.ConcreteDayViewModel;

import java.util.Objects;

import static com.orlinskas.notebook.Constants.AFTER_CREATE_OPEN_DAY;
import static com.orlinskas.notebook.Constants.DAY_ID;
import static com.orlinskas.notebook.Constants.IS_FULL_DISPLAY;

public class ConcreteDayView extends AppCompatActivity implements DayFragmentActions {
    private ProgressBar progressBar;
    private int dayID;
    private ConcreteDayViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_day);

        progressBar = findViewById(R.id.activity_concrete_day_pb);
        FloatingActionButton fab = findViewById(R.id.activity_concrete_day_fab);
        fab.setOnClickListener(view -> openCreateNotificationActivity());

        model = ViewModelProviders.of(this).get(ConcreteDayViewModel.class);

        model.getDaysData().observe(this, days -> updateUI());

        model.getRepositoryStatusData().observe(this, repositoryStatusEnum -> {
            if (repositoryStatusEnum == Enums.RepositoryStatus.LOADING) {
                progressBar.setVisibility(View.VISIBLE);
            }
            if (repositoryStatusEnum == Enums.RepositoryStatus.READY) {
                progressBar.setVisibility(View.INVISIBLE);
            }
        });

        model.getConnectionStatusData().observe(this, status -> {
            if (status == Enums.ConnectionStatus.CONNECTION_DONE) {
                doToast(Constants.REMOTE);
            }
            if (status== Enums.ConnectionStatus.CONNECTION_FAIL) {
                doToast(Constants.LOCAL);
            }
        });

        if(savedInstanceState != null) {
            dayID = savedInstanceState.getInt(DAY_ID);
        }
        else {
            dayID = Objects.requireNonNull(getIntent().getExtras()).getInt(DAY_ID);
        }
    }

    private void updateUI() {
        FragmentManager fm = getSupportFragmentManager();

        Fragment fragment = fm.findFragmentById(R.id.activity_concrete_day_container);
        if (fragment == null) {
            Bundle bundle = new Bundle();
            bundle.putInt(DAY_ID, dayID);
            bundle.putBoolean(IS_FULL_DISPLAY, true);

            fragment = new DayView();
            fragment.setArguments(bundle);

            fm.beginTransaction()
                    .add(R.id.activity_concrete_day_container, fragment)
                    .commit();
        } else {
            fm.beginTransaction().detach(fragment).attach(fragment).commit();
        }
    }

    private void doToast(String message) {
        if (!isFinishing()) {
            runOnUiThread(() -> ToastBuilder.doToast(getApplication().getApplicationContext(), message));
        }
    }

    public void openCreateNotificationActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateNotificationView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setAction(AFTER_CREATE_OPEN_DAY);
        startActivity(intent);
    }

    @Override
    public void openDay(int dayID) {
        //not specified in the technical specifications for this window
    }

    @Override
    public void deleteNotification(Notification notification) {
        model.deleteNotification(notification);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(DAY_ID, dayID);
    }
}