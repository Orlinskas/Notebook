package com.orlinskas.notebook.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.orlinskas.notebook.CoroutinesFunKt;
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

public class ConcreteDayViewModel extends ViewModel {
    private LiveData<List<Day>> daysData;
    private Job job;
    private CoroutineScope scope = CoroutinesFunKt.getIoScope();
    private NotificationRepository repository = new NotificationRepository();

    public ConcreteDayViewModel() {
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

    public LiveData<List<Day>> getDaysData() throws InterruptedException {
        if(daysData == null) {
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
        }
        return daysData;
    }

    public void deleteNotification(Notification notification) {
        job = BuildersKt.launch(Dispatchers::getIO, Dispatchers.getIO(), CoroutineStart.DEFAULT,
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

    @Override
    protected void onCleared() {
        super.onCleared();
        job.cancel(new CancellationException());
    }
}
