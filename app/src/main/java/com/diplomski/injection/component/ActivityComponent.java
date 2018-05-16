package com.diplomski.injection.component;


import com.diplomski.injection.module.ActivityModule;
import com.diplomski.injection.module.PresenterModule;
import com.diplomski.injection.module.RouterModule;
import com.diplomski.injection.scope.ForActivity;
import com.diplomski.ui.base.activities.BaseActivity;
import com.diplomski.ui.home.HomePresenter;
import com.diplomski.ui.home.HomeRouter;
import com.diplomski.ui.login.LoginPresenter;

import dagger.Component;


@ForActivity
@Component(
        dependencies = {
                ApplicationComponent.class
        },
        modules = {
                ActivityModule.class,
                PresenterModule.class,
                RouterModule.class
        }
)
public interface ActivityComponent extends ActivityComponentActivityInjects, ActivityComponentFragmentsInjects {

    final class Initializer {

        private Initializer() {
        }

        public static ActivityComponent init(final ApplicationComponent applicationComponent, final BaseActivity activity) {
            return DaggerActivityComponent.builder()
                    .applicationComponent(applicationComponent)
                    .activityModule(new ActivityModule(activity))
                    .build();
        }
    }

    HomeRouter getHomeRouter();

    HomePresenter getHomePresenter();

    LoginPresenter getLoginPresenter();


}

