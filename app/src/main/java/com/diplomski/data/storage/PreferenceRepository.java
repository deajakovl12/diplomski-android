package com.diplomski.data.storage;

public interface PreferenceRepository {

    void setApiKey(String apyKey);

    String getApyKey();

    void setUserId(long userId);

    long getUserId();

    void setLastRecordId(String recordId);

    String getLastRecordId();

    void setLat(double lat);
    void setLng(double lng);
    void setSpeed(float speed);

    float getLat();
    float getLng();
    float getSpeed();

}
