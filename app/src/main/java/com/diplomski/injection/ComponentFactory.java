package com.diplomski.injection;


import com.diplomski.application.TaskApplication;
import com.diplomski.injection.component.ActivityComponent;
import com.diplomski.injection.component.ApplicationComponent;
import com.diplomski.ui.base.activities.BaseActivity;

public final class ComponentFactory {

    private ComponentFactory() { }

    public static ApplicationComponent createApplicationComponent(final TaskApplication application) {
        return ApplicationComponent.Initializer.init(application);
    }

    public static ActivityComponent createActivityComponent(final TaskApplication application, final BaseActivity activity) {
        return ActivityComponent.Initializer.init(application.getApplicationComponent(), activity);
    }
}
