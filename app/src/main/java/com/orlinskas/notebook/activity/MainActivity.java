package com.orlinskas.notebook.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.builder.DaysBuilder;
import com.orlinskas.notebook.value.Day;
import com.orlinskas.notebook.fragment.DayFragment;
import com.orlinskas.notebook.fragment.DayFragmentActions;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.parceler.Parcels;

import java.util.List;

import static com.orlinskas.notebook.Constants.AFTER_CREATE_OPEN_MAIN;
import static com.orlinskas.notebook.Constants.PARCEL_DAY;
import static com.orlinskas.notebook.Constants.PARCEL_DAYS;

public class MainActivity extends AppCompatActivity implements DayFragmentActions {
    private List<Day> days;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.activity_main_pb);
        FloatingActionButton fab = findViewById(R.id.activity_main_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openCreateNotificationActivity();
            }
        });

        if (savedInstanceState != null) {
            days = Parcels.unwrap(savedInstanceState.getParcelable(PARCEL_DAYS));
        } else {
            new FindDaysTask().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("StaticFieldLeak")
    private class FindDaysTask extends AsyncTask<Void, Void, List<Day>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Day> doInBackground(Void... voids) {
            DaysBuilder DaysBuilder = new DaysBuilder();
            return DaysBuilder.findActual();
        }

        @Override
        protected void onPostExecute(List<Day> days) {
            super.onPostExecute(days);
            if (days.size() > 0) {
                MainActivity.this.days = days;
                showDaysList();
            } else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Failed to load", Toast.LENGTH_LONG);
                toast.show();
            }
            progressBar.setVisibility(View.INVISIBLE);
        }
    }

    private void showDaysList() {
        FragmentManager fm = getSupportFragmentManager();

        for (Day day : days) {
            Fragment fragment = fm.findFragmentById(R.id.activity_main_ll_days_container);
            if (fragment == null) {
                Bundle bundle = new Bundle();
                bundle.putParcelable(PARCEL_DAY, Parcels.wrap(day));

                fragment = new DayFragment();
                fragment.setArguments(bundle);

                fm.beginTransaction()
                        .add(R.id.activity_main_ll_days_container, fragment)
                        .commit();
            }
        }
    }

    @Override
    public void openDay(Day day) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(PARCEL_DAY, Parcels.wrap(day));
        Intent intent = new Intent(getApplicationContext(), ConcreteDayActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void openCreateNotificationActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.setAction(AFTER_CREATE_OPEN_MAIN);
        startActivity(intent);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(PARCEL_DAYS, Parcels.wrap(days));
    }
}
