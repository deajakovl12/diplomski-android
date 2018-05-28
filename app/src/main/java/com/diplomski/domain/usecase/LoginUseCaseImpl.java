package com.diplomski.domain.usecase;



import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.data.service.NetworkService;
import com.diplomski.data.storage.database.DatabaseHelper;

import io.reactivex.Completable;
import io.reactivex.Single;

public class LoginUseCaseImpl implements LoginUseCase {

    private final NetworkService networkService;
    private final DatabaseHelper databaseHelper;

    public LoginUseCaseImpl(NetworkService networkService, DatabaseHelper databaseHelper) {
        this.networkService = networkService;
        this.databaseHelper = databaseHelper;
    }

    @Override
    public Single<LoginApiResponse> loginUser(LoginRequest loginRequest) {
        return Single.defer(()-> networkService.loginUser(loginRequest));
    }

    @Override
    public Single<LoginApiResponse>  saveUserToDb(LoginApiResponse loginApiResponse) {
        return Single.defer(() -> databaseHelper.loginUser(loginApiResponse));
    }

    @Override
    public Single<LoginApiResponse> getUserFromDb() {
        return Single.defer(databaseHelper::getLoggedInUser);
    }

    @Override
    public Completable removeAllDataFromDb() {
        return Completable.defer(databaseHelper::removeAllDataFromDb);
    }
}
