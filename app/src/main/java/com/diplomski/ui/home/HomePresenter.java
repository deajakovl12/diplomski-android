package com.diplomski.ui.home;


import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;

public interface HomePresenter {

    void setView(HomeView view);

    void getMovieInfo();

    void dispose();

    void saveFullRecordToDb(FullRecordingInfo fullRecordingInfo);

    void saveRecordToDb(RecordInfo recordInfo, double distance);

    void checkDataForUpload();

    void uploadRecordsToServer();

    void checkDataForUploadLogout();

    void removeDataFromDb();

    void updateTraveledDistance();

    void saveLatLngSpeedToPref(double lat, double lng, float speed);

    float getLat();

    float getLng();

    float getSpeed();

}
