package com.orlinskas.notebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.value.Day;

import org.parceler.Parcels;

import static com.orlinskas.notebook.Constants.AFTER_CREATE_OPEN_DAY;
import static com.orlinskas.notebook.Constants.PARCEL_DAY;

public class ConcreteDayActivity extends AppCompatActivity {
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
            day = Parcels.unwrap(getIntent().getBundleExtra(PARCEL_DAY));
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
        outState.putParcelable(PARCEL_DAY, Parcels.wrap(day));
    }
}
