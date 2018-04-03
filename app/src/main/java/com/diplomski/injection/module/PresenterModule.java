package com.diplomski.injection.module;

import com.diplomski.data.api.converter.MovieAPIConverter;
import com.diplomski.domain.usecase.MovieUseCase;
import com.diplomski.injection.scope.ForActivity;
import com.diplomski.manager.StringManager;
import com.diplomski.ui.home.HomePresenter;
import com.diplomski.ui.home.HomePresenterImpl;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.reactivex.Scheduler;

import static com.diplomski.injection.module.ThreadingModule.OBSERVE_SCHEDULER;
import static com.diplomski.injection.module.ThreadingModule.SUBSCRIBE_SCHEDULER;


@Module
public final class PresenterModule {

    @ForActivity
    @Provides
    HomePresenter provideHomePresenter(@Named(SUBSCRIBE_SCHEDULER) Scheduler subscribeScheduler,
                                       @Named(OBSERVE_SCHEDULER) Scheduler observeScheduler, MovieUseCase movieUseCase, MovieAPIConverter movieAPIConverter, StringManager stringManager) {
        return new HomePresenterImpl(subscribeScheduler, observeScheduler, movieUseCase, movieAPIConverter, stringManager);
    }

}
