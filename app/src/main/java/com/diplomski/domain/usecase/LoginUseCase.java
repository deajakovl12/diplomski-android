package com.diplomski.domain.usecase;



import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;

import io.reactivex.Single;

public interface LoginUseCase {

    Single<LoginApiResponse> loginUser(LoginRequest loginRequest);

}
