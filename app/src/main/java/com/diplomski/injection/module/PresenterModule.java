package com.diplomski.injection.module;

import com.diplomski.data.api.converter.MovieAPIConverter;
import com.diplomski.data.storage.PreferenceRepository;
import com.diplomski.domain.usecase.LoginUseCase;
import com.diplomski.domain.usecase.MovieUseCase;
import com.diplomski.domain.usecase.RecordUseCase;
import com.diplomski.injection.scope.ForActivity;
import com.diplomski.manager.StringManager;
import com.diplomski.ui.home.HomePresenter;
import com.diplomski.ui.home.HomePresenterImpl;
import com.diplomski.ui.login.LoginPresenter;
import com.diplomski.ui.login.LoginPresenterImpl;

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
                                       @Named(OBSERVE_SCHEDULER) Scheduler observeScheduler,
                                       MovieUseCase movieUseCase,
                                       MovieAPIConverter movieAPIConverter,
                                       StringManager stringManager,
                                       RecordUseCase recordUseCase,
                                       LoginUseCase loginUseCase,
                                       PreferenceRepository preferences) {
        return new HomePresenterImpl(subscribeScheduler,
                observeScheduler,
                movieUseCase,
                movieAPIConverter,
                stringManager,
                recordUseCase,
                loginUseCase, preferences);
    }

    @ForActivity
    @Provides
    LoginPresenter provideLoginPresenter(@Named(SUBSCRIBE_SCHEDULER) Scheduler subscribeScheduler,
                                         @Named(OBSERVE_SCHEDULER) Scheduler observeScheduler, LoginUseCase loginUseCase) {
        return new LoginPresenterImpl(subscribeScheduler, observeScheduler, loginUseCase);
    }

}
