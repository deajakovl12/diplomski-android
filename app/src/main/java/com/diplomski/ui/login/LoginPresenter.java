package com.diplomski.ui.login;

import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;

public interface LoginPresenter {

    void setView(LoginView view);

    void loginUser(LoginRequest loginRequest);

    void dispose();

    void saveLoginUserToDb(LoginApiResponse loginApiResponse);

    void checkIfUserIsLoggedIn();

}
