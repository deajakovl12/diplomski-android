package com.diplomski.ui.login;


import com.diplomski.data.api.models.response.LoginApiResponse;

public interface LoginView {

    void loginSuccessfull(LoginApiResponse loginApiResponse);

    void loginFailure();

    void savedToDbucess(LoginApiResponse loginApiResponse);

    void userAlreadyLoggedIn(LoginApiResponse loginApiResponse);
}
