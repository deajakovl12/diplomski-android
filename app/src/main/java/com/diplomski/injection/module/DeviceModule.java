package com.diplomski.injection.module;

import com.diplomski.application.TaskApplication;
import com.diplomski.device.ApplicationInformation;
import com.diplomski.device.ApplicationInformationImpl;
import com.diplomski.device.DeviceInformation;
import com.diplomski.device.DeviceInformationImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public final class DeviceModule {

    @Provides
    @Singleton
    public DeviceInformation provideDeviceInformation() {
        return new DeviceInformationImpl();
    }

    @Provides
    @Singleton
    public ApplicationInformation provideApplicationInformation(final TaskApplication application) {
        return new ApplicationInformationImpl(application, application.getPackageManager());
    }
}
