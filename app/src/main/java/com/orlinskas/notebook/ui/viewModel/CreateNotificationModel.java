package com.orlinskas.notebook.ui.viewModel;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.Constants;
import com.orlinskas.notebook.CoroutinesFunKt;
import com.orlinskas.notebook.Enums;
import com.orlinskas.notebook.builder.ToastBuilder;
import com.orlinskas.notebook.entity.Notification;
import com.orlinskas.notebook.repository.NotificationRepository;

import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CancellationException;

import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Job;

public class CreateNotificationModel extends AndroidViewModel {
    private NotificationRepository repository = App.getInstance().getRepository();
    private LiveData<Enum<Enums.RepositoryStatus>> repositoryStatusData = repository.getRepositoryStatusData();
    private LiveData<Enum<Enums.ConnectionStatus>> connectionStatusData = repository.getConnectionStatusData();
    private Job job;
    private CoroutineScope scope = CoroutinesFunKt.getIoScope();
    private Handler handler = new Handler();

    public CreateNotificationModel(@NonNull Application application) {
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
                            showConnectionStatus();
                        }
                    });
                    return scope.getCoroutineContext();
                });
    }

    public LiveData<Enum<Enums.RepositoryStatus>> getRepositoryStatusData() {
        return repositoryStatusData;
    }

    public void createNotification(Notification notification) {
        job = BuildersKt.launch(scope, scope.getCoroutineContext(), CoroutineStart.DEFAULT,
                (scope, continuation) -> { repository.insert(notification, new Continuation<Unit>() {
                    @NotNull
                    @Override
                    public CoroutineContext getContext() {
                        return scope.getCoroutineContext();
                    }
                    @Override
                    public void resumeWith(@NotNull Object o) {
                        showConnectionStatus();
                    }
                });
                    return scope.getCoroutineContext();
                });
    }

    private void showConnectionStatus() {
        Enum<Enums.ConnectionStatus> status = connectionStatusData.getValue();
        if (status == Enums.ConnectionStatus.CONNECTION_DONE) {
            doToast(Constants.REMOTE);
        }
        if (status== Enums.ConnectionStatus.CONNECTION_FAIL) {
            doToast(Constants.LOCAL);
        }
    }

    private void doToast(String message) {
        handler.post(() -> ToastBuilder.doToast(getApplication().getBaseContext(), message));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        job.cancel(new CancellationException());
    }
}
