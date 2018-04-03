package com.diplomski.injection.module;

import com.diplomski.application.TaskApplication;
import com.diplomski.manager.StringManager;
import com.diplomski.manager.StringManagerImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class ManagerModule {

    @Provides
    @Singleton
    StringManager provideStringManager(final TaskApplication application) {
        return new StringManagerImpl(application.getResources());
    }
}
