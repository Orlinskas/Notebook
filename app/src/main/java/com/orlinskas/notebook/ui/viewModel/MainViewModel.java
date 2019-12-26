package com.orlinskas.notebook.ui.viewModel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.orlinskas.notebook.CoroutinesFunKt;
import com.orlinskas.notebook.Enums;
import com.orlinskas.notebook.builder.ToastBuilder;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.repository.NotificationRepository;
import com.orlinskas.notebook.value.Day;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.concurrent.CancellationException;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.Job;

public class MainViewModel extends AndroidViewModel {
    private NotificationRepository repository = new NotificationRepository();
    private LiveData<Enum<Enums.RepositoryStatus>> repositoryStatusData = repository.getRepositoryStatusData();
    private LiveData<Enum<Enums.ConnectionStatus>> connectionStatusData = repository.getConnectionStatusData();
    private LiveData<List<Day>> daysData;
    private Job job;
    private CoroutineScope scope = CoroutinesFunKt.getIoScope();

    public MainViewModel(@NonNull Application application) {
        super(application);
        job = BuildersKt.launch(scope, scope.getCoroutineContext(), CoroutineStart.DEFAULT,
                (scope, continuation) -> {
                    repository.findActual(System.currentTimeMillis(), new Continuation<Unit>() {
                        @NotNull
                        @Override
                        public CoroutineContext getContext() {
                            return scope.getCoroutineContext();
                        }
                        @Override
                        public void resumeWith(@NotNull Object o) {

                        }
                    });
                    return scope.getCoroutineContext();
                });
    }

    public LiveData<List<Day>> getDaysData()  {
        if(daysData == null) {
            try {
                daysData = BuildersKt.runBlocking(scope.getCoroutineContext(),
                        (scope, continuation) -> repository.fastStart(new Continuation<MutableLiveData<List<Day>>>() {
                            @NotNull
                            @Override
                            public CoroutineContext getContext() {
                                return scope.getCoroutineContext();
                            }
                            @Override
                            public void resumeWith(@NotNull Object o) {

                            }
                        }));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return daysData;
    }

    public void deleteNotification(Notification notification) {
        job = BuildersKt.launch(scope, scope.getCoroutineContext(), CoroutineStart.DEFAULT,
                (scope, coroutine) -> repository.delete(notification, new Continuation<Unit>() {
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return Dispatchers.getIO();
                    }
                    @Override
                    public void resumeWith(@NotNull Object o) {

                    }
                }));
    }

    public LiveData<Enum<Enums.RepositoryStatus>> getRepositoryStatusData() {
        return repositoryStatusData;
    }

    public LiveData<Enum<Enums.ConnectionStatus>> getConnectionStatusData() {
        return connectionStatusData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        job.cancel(new CancellationException());
    }

    private void showConnectionStatus() {
       Enum<Enums.ConnectionStatus> status = connectionStatusData.getValue();
        if (status == Enums.ConnectionStatus.CONNECTION_DONE) {
            doToast("Connection Done");
        }
        if (status== Enums.ConnectionStatus.CONNECTION_FAIL) {
            doToast("Connection Fail");
        }
    }

    private void doToast(String message) {
        ToastBuilder.doToast(getApplication().getApplicationContext(), message);
    }
}
