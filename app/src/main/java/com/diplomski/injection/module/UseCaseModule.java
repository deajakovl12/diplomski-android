package com.diplomski.injection.module;

import com.diplomski.data.service.NetworkService;
import com.diplomski.data.storage.TemplatePreferences;
import com.diplomski.domain.usecase.MovieUseCase;
import com.diplomski.domain.usecase.MovieUseCaseImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class UseCaseModule {


    @Provides
    @Singleton
    MovieUseCase providePersonUseCase(final TemplatePreferences preferences, final NetworkService networkService) {
        return new MovieUseCaseImpl(networkService, preferences);
    }

}
