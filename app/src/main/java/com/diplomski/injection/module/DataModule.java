package com.diplomski.injection.module;

import android.provider.Settings;

import com.diplomski.application.TaskApplication;
import com.diplomski.data.storage.PreferenceRepository;
import com.diplomski.data.storage.SecureSharedPreferences;
import com.diplomski.data.storage.TemplatePreferences;
import com.diplomski.data.storage.database.DatabaseHelper;
import com.diplomski.data.storage.database.DatabaseHelperImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class DataModule {

    public static final String PREFS_NAME = "diplomskiSecureStorage";

    public static final int DATABASE_VERSION = 7;
    public static final String DATABASE_NAME = "task.db";

    @Singleton
    @Provides
    TemplatePreferences provideTemplatePreferences(final SecureSharedPreferences securePreferences) {
        return TemplatePreferences.create(securePreferences);
    }

    @Provides
    @Singleton
    PreferenceRepository providePreferenceRepository(final TemplatePreferences templatePreferences) {
        return templatePreferences;
    }

    @Provides
    @Singleton
    public SecureSharedPreferences provideSecureSharedPreferences(final TaskApplication context) {
        final String androidSecret = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return new SecureSharedPreferences(context, context.getSharedPreferences(PREFS_NAME, 0), androidSecret);
    }

    @Provides
    @Singleton
    DatabaseHelper provideDatabaseHelper(final TaskApplication application, final PreferenceRepository preferenceRepository) {
        return new DatabaseHelperImpl(application, DATABASE_NAME, DATABASE_VERSION, preferenceRepository);
    }

}
