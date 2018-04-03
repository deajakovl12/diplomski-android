package com.diplomski.device;

import android.os.Build;

public class DeviceInformationImpl implements DeviceInformation {

    @Override
    public int getOsVersionInt() {
        return Build.VERSION.SDK_INT;
    }

}
