package com.diplomski.injection.component;


import com.diplomski.ui.home.HomeActivity;
import com.diplomski.ui.login.LoginActivity;

public interface ActivityComponentActivityInjects {

    void inject(HomeActivity homeActivity);
    void inject(LoginActivity loginActivity);
}
