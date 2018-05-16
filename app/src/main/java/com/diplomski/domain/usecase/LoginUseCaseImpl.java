package com.diplomski.domain.usecase;



import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.data.service.NetworkService;

import io.reactivex.Single;

public class LoginUseCaseImpl implements LoginUseCase {

    private final NetworkService networkService;

    public LoginUseCaseImpl(NetworkService networkService) {
        this.networkService = networkService;
    }

    @Override
    public Single<LoginApiResponse> loginUser(LoginRequest loginRequest) {
        return Single.defer(()-> networkService.loginUser(loginRequest));
    }
}
