package com.diplomski.ui.login;

import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.domain.usecase.LoginUseCase;
import com.diplomski.ui.base.presenter.BasePresenter;

import javax.inject.Named;

import io.reactivex.Scheduler;
import timber.log.Timber;

import static com.diplomski.injection.module.ThreadingModule.OBSERVE_SCHEDULER;
import static com.diplomski.injection.module.ThreadingModule.SUBSCRIBE_SCHEDULER;

public class LoginPresenterImpl extends BasePresenter implements  LoginPresenter{

    private LoginView view;
    private final Scheduler subscribeScheduler;

    private final Scheduler observeScheduler;

    private final LoginUseCase loginUseCase;

    public LoginPresenterImpl(@Named(SUBSCRIBE_SCHEDULER) final Scheduler subscribeScheduler,
                             @Named(OBSERVE_SCHEDULER) final Scheduler observeScheduler, final LoginUseCase loginUseCase) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.loginUseCase = loginUseCase;
    }

    @Override
    public void setView(LoginView view) {
        this.view = view;
    }

    @Override
    public void loginUser(LoginRequest loginRequest){
        if (view != null) {
            addDisposable(loginUseCase.loginUser(loginRequest)
                    .subscribeOn(subscribeScheduler)
                    .observeOn(observeScheduler)
                    .subscribe(this::onLoginSuccess, this::onLoginFailure));
        }
    }

    private void onLoginSuccess(LoginApiResponse loginApiResponse) {
        if(view !=null){
            if(loginApiResponse != null && loginApiResponse.username != null && loginApiResponse.isAdmin == 1){
                view.loginSuccessfull(loginApiResponse);
            }else{
                view.loginFailure();
            }
        }
    }

    private void onLoginFailure(Throwable throwable) {
        Timber.e(throwable.getMessage());
    }
}
