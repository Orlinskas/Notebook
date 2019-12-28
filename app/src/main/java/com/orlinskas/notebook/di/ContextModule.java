package com.orlinskas.notebook.di;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

@NotificationScope
@Module
public class ContextModule {

    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context context() {
        return context;
    }

    @ApplicationContext
    @Provides
    public Context applicationContext() {
        return context.getApplicationContext();
    }
}

