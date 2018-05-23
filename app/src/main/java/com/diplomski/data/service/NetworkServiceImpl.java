package com.diplomski.data.service;


import com.diplomski.data.api.models.request.FullRecordInfoRequest;
import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.data.api.models.response.MovieApiResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.Response;

public final class NetworkServiceImpl implements NetworkService {

    private final TemplateAPI templateAPI;

    public NetworkServiceImpl(final TemplateAPI templateAPI) {
        this.templateAPI = templateAPI;
    }


    @Override
    public Single<List<MovieApiResponse>> movieInfo() {
        return Single.defer(() -> templateAPI.movieInfo());
    }

    @Override
    public Single<LoginApiResponse> loginUser(LoginRequest loginRequest) {
        return Single.defer(() -> templateAPI.loginUser(loginRequest));
    }

    @Override
    public Single<Response<Void>> uploadRecordsToServer(List<FullRecordInfoRequest> fullRecordInfoRequests) {
        return Single.defer(() -> templateAPI.uploadRecordsToServer(fullRecordInfoRequests));
    }
}
