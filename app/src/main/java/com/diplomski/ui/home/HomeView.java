package com.diplomski.ui.home;


import com.diplomski.data.api.models.response.MovieApiResponse;

public interface HomeView {

    void showData(MovieApiResponse movieInfo);

    void recordingStarted();

    void needDataUpload(Boolean notSent);

    void resetAllToStart();

    void showErroUploadMessage();

    void hasData(Boolean notSent);

    void logoutUser();
}
