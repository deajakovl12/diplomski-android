package com.diplomski.data.service;


import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.data.api.models.response.MovieApiResponse;

import java.util.List;

import io.reactivex.Single;

public interface NetworkService {

    Single<List<MovieApiResponse>> movieInfo();

    Single<LoginApiResponse> loginUser(LoginRequest loginRequest);

}
