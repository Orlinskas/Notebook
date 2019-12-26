package com.orlinskas.notebook.ui.viewModel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.CoroutinesFunKt;
import com.orlinskas.notebook.Enums;
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
import kotlinx.coroutines.Job;

public class MainViewModel extends ViewModel {
    private LiveData<List<Day>> daysData;
    private NotificationRepository repository = new NotificationRepository();
    private Job job = null;
    private CoroutineScope scope = CoroutinesFunKt.getIoScope();
    public LiveData<Enum<Enums.RepositoryStatus>> repositoryStatusData = App.instance.getRepositoryStatusData();
    public LiveData<Enum<Enums.ConnectionStatus>> connectionStatusData = App.instance.getConnectionStatusData();

    public MainViewModel() {
        super();
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
                            //daysData = (LiveData<List<Day>>) o;
                        }
                    });
                    return scope.getCoroutineContext();
                });
    }

    public LiveData<List<Day>> getDaysData() {
        return daysData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        job.cancel(new CancellationException());
    }
}
