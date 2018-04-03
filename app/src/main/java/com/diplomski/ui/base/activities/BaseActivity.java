package com.diplomski.ui.base.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.diplomski.application.TaskApplication;
import com.diplomski.injection.ComponentFactory;
import com.diplomski.injection.component.ActivityComponent;

public abstract class BaseActivity extends Activity {

    private ActivityComponent activityComponent;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final TaskApplication taskApplication = (TaskApplication) getApplication();
        initActivityComponent(taskApplication);
        inject(activityComponent);
    }

    public final ActivityComponent getActivityComponent(final TaskApplication taskApplication) {
        if (activityComponent == null) {
            initActivityComponent(taskApplication);
        }
        return activityComponent;
    }

    private void initActivityComponent(final TaskApplication taskApplication) {
        activityComponent = ComponentFactory.createActivityComponent(taskApplication, this);
    }

    protected abstract void inject(ActivityComponent activityComponent);
}
