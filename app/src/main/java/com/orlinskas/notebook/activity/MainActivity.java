package com.orlinskas.notebook.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.orlinskas.notebook.App;
import com.orlinskas.notebook.CoroutinesFunKt;
import com.orlinskas.notebook.R;
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
import kotlinx.coroutines.Job;

import static com.orlinskas.notebook.Constants.DAY_ID;
import static com.orlinskas.notebook.Constants.IS_FULL_DISPLAY;

public class MainActivity extends AppCompatActivity implements DayFragmentActions, ConnectionCallBack {
    private ProgressBar progressBar;
    private LiveData<List<Day>> daysData;
    private NotificationRepository repository = new NotificationRepository(this);
    private Job job = null;
    private CoroutineScope scope = CoroutinesFunKt.getIoScope();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.activity_main_pb);
        FloatingActionButton fab = findViewById(R.id.activity_main_fab);
        fab.setOnClickListener(view -> openCreateNotificationActivity());

        job = BuildersKt.launch(scope, scope.getCoroutineContext(), CoroutineStart.DEFAULT,
                (scope, continuation) -> {
                    repository.findActual(System.currentTimeMillis(), new Continuation<MutableLiveData<List<Day>>>() {
                        @NotNull
                        @Override
                        public CoroutineContext getContext() {
                            return MainActivity.this.scope.getCoroutineContext();
                        }

                        @Override
                        public void resumeWith(@NotNull Object o) {
                            //daysData = (LiveData<List<Day>>) o;
                        }
                    });
                    return scope.getCoroutineContext();
                });

        //getLifecycle().addObserver(repository);
        daysData = App.getInstance().getDaysLiveData();
        daysData.observe(this, days -> {
                MainActivity.this.showDaysList();
        });
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

    @Override
    public Object failConnection(@NotNull Continuation<? super Unit> continuation) {
        return null;
    }

    @Override
    public Object doneConnection(@NotNull Continuation<? super Unit> continuation) {
        return null;
    }

    private void showDaysList() {
        FragmentManager fm = getSupportFragmentManager();

        for (Day day : Objects.requireNonNull(daysData.getValue())) {
            Fragment fragment = fm.findFragmentById(R.id.activity_main_ll_days_container);
            if (fragment == null) {
                Bundle bundle = new Bundle();
                bundle.putInt(DAY_ID, daysData.getValue().indexOf(day));

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

    @Override
    public void openDay(int dayID) {
        Bundle bundle = new Bundle();
        bundle.putInt(DAY_ID, dayID);
        bundle.putBoolean(IS_FULL_DISPLAY, true);
        Intent intent = new Intent(getApplicationContext(), ConcreteDayActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @Override
    public void deleteNotification(Notification notification) {
        //not specified in the technical specifications for this window
    }

    public void openCreateNotificationActivity() {
        Intent intent = new Intent(getApplicationContext(), CreateNotificationActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        job.cancel(new CancellationException());
    }
}
