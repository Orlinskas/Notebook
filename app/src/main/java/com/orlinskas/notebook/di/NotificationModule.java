package com.orlinskas.notebook.di;

import android.content.Context;

import androidx.room.Room;

import com.orlinskas.notebook.database.MyDatabase;
import com.orlinskas.notebook.repository.NotificationRepository;
import com.orlinskas.notebook.repository.Synchronizer;
import com.orlinskas.notebook.service.ApiFactory;
import com.orlinskas.notebook.service.NotificationApiService;

import dagger.Module;
import dagger.Provides;

@NotificationScope
@Module
public class NotificationModule {
    @Provides
    public NotificationRepository notificationRepository(MyDatabase database, NotificationApiService apiService, Synchronizer synchronizer) {
        return new NotificationRepository(database, apiService, synchronizer);
    }

    @Provides
    public MyDatabase database(Context context) {
        return Room.databaseBuilder(context, MyDatabase.class, "notification").build();
    }

    @Provides
    public NotificationApiService notificationApiService() {
        return ApiFactory.INSTANCE.getNotificationApi();
    }

    @Provides
    public Synchronizer synchronizer() {
        return new Synchronizer();
    }

}
