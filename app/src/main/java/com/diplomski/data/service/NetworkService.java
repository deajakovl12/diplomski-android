package com.diplomski.data.service;


import com.diplomski.data.api.models.request.FullRecordInfoRequest;
import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.data.api.models.response.MovieApiResponse;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import retrofit2.Response;

public interface NetworkService {

    Single<List<MovieApiResponse>> movieInfo();

    Single<LoginApiResponse> loginUser(LoginRequest loginRequest);

    Single<Response<Void>> uploadRecordsToServer(List<FullRecordInfoRequest> fullRecordInfoRequests);

}
