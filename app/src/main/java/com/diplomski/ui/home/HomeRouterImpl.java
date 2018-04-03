package com.diplomski.ui.home;

import android.app.Activity;

public class HomeRouterImpl implements HomeRouter {

    private final Activity activity;

    public HomeRouterImpl(final Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onDeviceDetails(final long deviceId) {
        //activity.startActivity(ClaimDeviceActivity.createIntent(activity, deviceId));
    }
}
