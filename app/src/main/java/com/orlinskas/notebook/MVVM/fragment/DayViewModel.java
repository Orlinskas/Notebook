package com.orlinskas.notebook.MVVM.fragment;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.orlinskas.notebook.App;
import com.orlinskas.notebook.CoroutinesFunKt;
import com.orlinskas.notebook.Enums;
import com.orlinskas.notebook.repository.NotificationRepository;
import com.orlinskas.notebook.MVVM.model.Day;

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

public class DayViewModel extends AndroidViewModel {
    private NotificationRepository repository = App.getInstance().getRepository();
    private LiveData<Enum<Enums.RepositoryStatus>> repositoryStatusData = repository.getRepositoryStatusData();
    private Job job;

    public DayViewModel(@NonNull Application application) {
        super(application);
        CoroutineScope scope = CoroutinesFunKt.getIoScope();

        job = BuildersKt.launch(scope, scope.getCoroutineContext(), CoroutineStart.DEFAULT,
                (coroutineScope, continuation) -> {
                    repository.findActual(System.currentTimeMillis(), new Continuation<Unit>() {
                        @NotNull
                        @Override
                        public CoroutineContext getContext() {
                            return coroutineScope.getCoroutineContext();
                        }
                        @Override
                        public void resumeWith(@NotNull Object o) {

                        }
                    });
                    return coroutineScope.getCoroutineContext();
                });
    }

    LiveData<List<Day>> getDaysData()  {
        return repository.getDaysData();
    }

    public LiveData<Enum<Enums.RepositoryStatus>> getRepositoryStatusData() {
        return repositoryStatusData;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        job.cancel(new CancellationException());
    }
}