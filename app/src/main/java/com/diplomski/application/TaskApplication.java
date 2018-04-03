package com.diplomski.application;

import android.app.Application;

import com.diplomski.injection.ComponentFactory;
import com.diplomski.injection.component.ApplicationComponent;

import timber.log.Timber;

public final class TaskApplication extends Application {

    private ApplicationComponent applicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        applicationComponent = ComponentFactory.createApplicationComponent(this);
        applicationComponent.inject(this);
        Timber.plant(new Timber.DebugTree());
    }

    public ApplicationComponent getApplicationComponent() {
        return applicationComponent;
    }
}
