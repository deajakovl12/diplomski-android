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

    @Override
    public void saveLoginUserToDb(LoginApiResponse loginApiResponse) {
        addDisposable(loginUseCase.saveUserToDb(loginApiResponse)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(this::onSaveUserToDbSuccess, this::onSaveUserToDbFailure));
    }

    private void onSaveUserToDbFailure(Throwable throwable) {
        Timber.e(throwable.getMessage() +  " ");
    }

    private void onSaveUserToDbSuccess(LoginApiResponse loginApiResponse) {
        if(view != null){
            view.savedToDbucess(loginApiResponse);
        }
    }

    @Override
    public void checkIfUserIsLoggedIn() {
        addDisposable(loginUseCase.getUserFromDb()
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(this::onGetUserFromDbSuccess, this::onGetUserFromDbFailure));
    }

    private void onGetUserFromDbSuccess(LoginApiResponse loginApiResponse) {
        if(view != null){
            view.userAlreadyLoggedIn(loginApiResponse);
        }
    }

    private void onGetUserFromDbFailure(Throwable throwable) {
        Timber.e("ERROR get user " + throwable.getMessage());
    }
}
