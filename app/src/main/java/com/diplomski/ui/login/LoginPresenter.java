package com.diplomski.ui.login;

import com.diplomski.data.api.models.request.LoginRequest;

public interface LoginPresenter {

    void setView(LoginView view);

    void loginUser(LoginRequest loginRequest);

    void dispose();
}
