package com.diplomski.injection.component;

import com.diplomski.application.TaskApplication;
import com.diplomski.data.api.converter.MovieAPIConverter;
import com.diplomski.data.service.NetworkService;
import com.diplomski.data.storage.TemplatePreferences;
import com.diplomski.device.ApplicationInformation;
import com.diplomski.device.DeviceInformation;
import com.diplomski.domain.usecase.MovieUseCase;
import com.diplomski.injection.module.ApiModule;
import com.diplomski.injection.module.ApplicationModule;
import com.diplomski.injection.module.DataModule;
import com.diplomski.injection.module.DeviceModule;
import com.diplomski.injection.module.ManagerModule;
import com.diplomski.injection.module.ThreadingModule;
import com.diplomski.injection.module.UseCaseModule;
import com.diplomski.manager.StringManager;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Component;

import io.reactivex.Scheduler;
import okhttp3.OkHttpClient;

import static com.diplomski.injection.module.ThreadingModule.OBSERVE_SCHEDULER;
import static com.diplomski.injection.module.ThreadingModule.SUBSCRIBE_SCHEDULER;


@Singleton
@Component(
        modules = {
                ApplicationModule.class,
                ApiModule.class,
                ManagerModule.class,
                DataModule.class,
                ThreadingModule.class,
                UseCaseModule.class,
                DeviceModule.class
        }
)

public interface ApplicationComponent extends ApplicationComponentInjects {

    final class Initializer {

        private Initializer() {
        }

        public static ApplicationComponent init(final TaskApplication taskApplication) {
            return DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(taskApplication))
                    .apiModule(new ApiModule())
                    .build();
        }
    }

    @Named(OBSERVE_SCHEDULER)
    Scheduler getObserveScheduler();

    @Named(SUBSCRIBE_SCHEDULER)
    Scheduler getSubscribeScheduler();

    StringManager getStringManager();

    MovieUseCase getMovieUseCase();

    OkHttpClient getOkHttpClient();

    DeviceInformation getDeviceInformation();

    ApplicationInformation getApplicationInformation();

    MovieAPIConverter getMovieApiConverter();

    TemplatePreferences getTemplatePreferences();

    NetworkService getNetworkService();
}
