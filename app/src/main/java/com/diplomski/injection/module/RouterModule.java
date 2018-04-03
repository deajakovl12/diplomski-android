package com.diplomski.injection.module;

import android.app.Activity;

import com.diplomski.injection.scope.ForActivity;
import com.diplomski.ui.home.HomeRouter;
import com.diplomski.ui.home.HomeRouterImpl;

import dagger.Module;
import dagger.Provides;

@Module
public final class RouterModule {

    @ForActivity
    @Provides
    HomeRouter provideHomeRouter(final Activity activity) {
        return new HomeRouterImpl(activity);
    }

}
