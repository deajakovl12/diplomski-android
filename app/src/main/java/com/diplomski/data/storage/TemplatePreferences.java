package com.diplomski.data.storage;

import android.content.SharedPreferences;

public final class TemplatePreferences implements PreferenceRepository {

    private static final String KEY_APY_KEY = "key_api_key";

    private static final String KEY_LAST_RECORD_ID = "key_last_record_id";

    private static final String EMPTY_STRING = "";

    private static final long EMPTY_USER_ID = 0;

    private static final String KEY_USER_ID = "key_user_id";

    private static final String KEY_GPS_LAT = "key_gps_lat";
    private static final String KEY_GPS_LNG = "key_gps_lng";
    private static final String KEY_GPS_SPEED = "key_gps_speed";



    private final SharedPreferences secureDelegate;


    public static TemplatePreferences create(final SharedPreferences secureDelegate) {
        return new TemplatePreferences(secureDelegate);
    }

    private TemplatePreferences(final SharedPreferences secureDelegate) {
        this.secureDelegate = secureDelegate;
    }

    @Override
    public void setUserId(final long userId) {
        secureDelegate.edit().putLong(KEY_USER_ID, userId).apply();
    }

    @Override
    public long getUserId() {
        return secureDelegate.getLong(KEY_USER_ID, EMPTY_USER_ID);
    }

    @Override
    public void setApiKey(final String apyKey) {
        secureDelegate.edit().putString(KEY_APY_KEY, apyKey).apply();
    }

    @Override
    public String getApyKey() {
        return secureDelegate.getString(KEY_APY_KEY, EMPTY_STRING);

    }

    @Override
    public void setLastRecordId(String recordId) {
        secureDelegate.edit().putString(KEY_LAST_RECORD_ID, recordId).apply();
    }

    @Override
    public String getLastRecordId() {
        return secureDelegate.getString(KEY_LAST_RECORD_ID, EMPTY_STRING);
    }

    @Override
    public void setLat(double lat) {
        secureDelegate.edit().putFloat(KEY_GPS_LAT, (float) lat).apply();

    }

    @Override
    public void setLng(double lng) {
        secureDelegate.edit().putFloat(KEY_GPS_LNG, (float) lng).apply();

    }

    @Override
    public void setSpeed(float speed) {
        secureDelegate.edit().putFloat(KEY_GPS_SPEED, speed).apply();

    }

    @Override
    public float getLat() {
        return secureDelegate.getFloat(KEY_GPS_LAT, 0);
    }

    @Override
    public float getLng() {
        return secureDelegate.getFloat(KEY_GPS_LNG, 0);
    }

    @Override
    public float getSpeed() {
        return secureDelegate.getFloat(KEY_GPS_SPEED, 0);
    }
}
