package com.orlinskas.notebook.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orlinskas.notebook.Constants;
import com.orlinskas.notebook.Enums;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.builder.ToastBuilder;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.fragment.DayFragment;
import com.orlinskas.notebook.fragment.DayFragmentActions;
import com.orlinskas.notebook.ui.viewModel.MainViewModel;
import com.orlinskas.notebook.value.Day;

import java.util.List;

import static com.orlinskas.notebook.Constants.DAY_ID;
import static com.orlinskas.notebook.Constants.IS_FULL_DISPLAY;

public class MainView extends AppCompatActivity implements DayFragmentActions {
    private ProgressBar progressBar;
    private MainViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.activity_main_pb);
        FloatingActionButton fab = findViewById(R.id.activity_main_fab);
        fab.setOnClickListener(view -> openCreateNotificationActivity());

        model = ViewModelProviders.of(this).get(MainViewModel.class);

        model.getDaysData().observe(this, days ->
                updateUI());

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
    }

    private void openCreateNotificationActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateNotificationView.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    public void updateUI() {
        List<Day> days = model.getDaysData().getValue();

        FragmentManager fm = getSupportFragmentManager();

        if(days != null) {
            for (int i = 0; i < days.size(); i++) {
                Fragment fragment = fm.findFragmentById(R.id.activity_main_ll_days_container);
                if (fragment == null) {
                    Bundle bundle = new Bundle();
                    bundle.putInt(DAY_ID, i);

                    fragment = new DayFragment();
                    fragment.setArguments(bundle);

                    fm.beginTransaction()
                            .add(R.id.activity_main_ll_days_container, fragment)
                            .commit();
                } else {
                    fm.beginTransaction().detach(fragment).attach(fragment).commit();
                }
            }
        }
    }

    private void doToast(String message) {
        if (!isFinishing()) {
            runOnUiThread(() -> ToastBuilder.doToast(getApplication().getApplicationContext(), message));
        }
    }

    @Override
    public void openDay(int dayID) {
        Bundle bundle = new Bundle();
        bundle.putInt(DAY_ID, dayID);
        bundle.putBoolean(IS_FULL_DISPLAY, true);
        Intent intent = new Intent(getBaseContext(), ConcreteDayView.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void deleteNotification(Notification notification) {
        //not specified in the technical specifications for this window
    }
}
