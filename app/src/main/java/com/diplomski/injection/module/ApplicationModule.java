package com.diplomski.injection.module;

import com.diplomski.application.TaskApplication;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class ApplicationModule {

    private final TaskApplication taskApplication;

    public ApplicationModule(final TaskApplication taskApplication) {
        this.taskApplication = taskApplication;
    }

    @Provides
    @Singleton
    TaskApplication provideApplication() {
        return taskApplication;
    }
}
