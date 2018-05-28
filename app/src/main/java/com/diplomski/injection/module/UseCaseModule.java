package com.diplomski.injection.module;

import com.diplomski.data.service.NetworkService;
import com.diplomski.data.storage.TemplatePreferences;
import com.diplomski.data.storage.database.DatabaseHelper;
import com.diplomski.domain.usecase.LoginUseCase;
import com.diplomski.domain.usecase.LoginUseCaseImpl;
import com.diplomski.domain.usecase.MovieUseCase;
import com.diplomski.domain.usecase.MovieUseCaseImpl;
import com.diplomski.domain.usecase.RecordUseCase;
import com.diplomski.domain.usecase.RecordUseCaseImpl;

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

    @Provides
    @Singleton
    RecordUseCase provideRecordUseCase(final TemplatePreferences preferences,
                                       final NetworkService networkService,
                                       final DatabaseHelper databaseHelper) {
        return new RecordUseCaseImpl(networkService, preferences, databaseHelper);
    }

    @Provides
    @Singleton
    LoginUseCase provideLoginUseCase(final NetworkService networkService, final DatabaseHelper databaseHelper) {
        return new LoginUseCaseImpl(networkService, databaseHelper);
    }

}
