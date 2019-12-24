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
import com.orlinskas.notebook.CoroutinesFunKt;
import com.orlinskas.notebook.R;
import com.orlinskas.notebook.builder.ToastBuilder;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.fragment.DayFragment;
import com.orlinskas.notebook.fragment.DayFragmentActions;
import com.orlinskas.notebook.repository.NotificationRepository;
import com.orlinskas.notebook.value.Day;

import org.jetbrains.annotations.NotNull;
import org.parceler.Parcels;

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
import static com.orlinskas.notebook.Constants.IS_FULL_DISPLAY;
import static com.orlinskas.notebook.Constants.PARCEL_DAY;

public class ConcreteDayActivity extends AppCompatActivity implements DayFragmentActions, ViewModel {
    private ProgressBar progressBar;
    private Day day;
    private Job job = null;
    private CoroutineScope scope = CoroutinesFunKt.getScope();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concrete_day);

        progressBar = findViewById(R.id.activity_concrete_day_pb);
        FloatingActionButton fab = findViewById(R.id.activity_concrete_day_fab);
        fab.setOnClickListener(view -> openCreateNotificationActivity());

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
        //not specified in the technical specifications for this window
    }

    @Override
    public void deleteNotification(Notification notification) {
        progressBar.setVisibility(View.VISIBLE);

        NotificationRepository repository = new NotificationRepository(this);

        job = BuildersKt.launch(Dispatchers::getIO, Dispatchers.getIO(), CoroutineStart.DEFAULT,
                (scope, coroutine) -> repository.delete(notification, new Continuation<Unit>() {
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return Dispatchers.getIO();
                    }
                    @Override
                    public void resumeWith(@NotNull Object o) {}
                }));
    }

    @Override
    public Object failConnection(@NotNull Continuation<? super Unit> o) {
        job = BuildersKt.launch(scope, scope.getCoroutineContext(), CoroutineStart.DEFAULT,
                (scope, continuation) -> {
                    ToastBuilder.doToast(getApplicationContext(), "Удаленно локально");
                    return null;
                });
        return null;
    }

    @Override
    public Object doneConnection(@NotNull Continuation<? super Unit> o) {
        job = BuildersKt.launch(scope, scope.getCoroutineContext(), CoroutineStart.DEFAULT,
                (scope, continuation) -> {
                    ToastBuilder.doToast(getApplicationContext(), "Удаленно на сервере");
                    return null;
                });
        return null;
    }

    @Override
    public Object updateUI(@NotNull Continuation<? super Unit> o) {
        job = BuildersKt.launch(scope, scope.getCoroutineContext(), CoroutineStart.DEFAULT,
                (scope, continuation) -> {
            progressBar.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return null;
        });
        return null;

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(job != null) {
            job.cancel(new CancellationException());
        }
    }
}