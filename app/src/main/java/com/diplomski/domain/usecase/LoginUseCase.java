package com.diplomski.domain.usecase;



import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface LoginUseCase {

    Single<LoginApiResponse> loginUser(LoginRequest loginRequest);

    Single<LoginApiResponse>  saveUserToDb(LoginApiResponse loginApiResponse);

    Single<LoginApiResponse> getUserFromDb();

    Completable removeAllDataFromDb();

}
