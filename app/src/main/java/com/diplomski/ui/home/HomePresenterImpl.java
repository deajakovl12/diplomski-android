package com.diplomski.ui.home;

import com.diplomski.R;
import com.diplomski.data.api.converter.MovieAPIConverter;
import com.diplomski.domain.model.MovieInfo;
import com.diplomski.domain.usecase.MovieUseCase;
import com.diplomski.manager.StringManager;
import com.diplomski.ui.base.presenter.BasePresenter;

import java.util.List;

import javax.inject.Named;


import io.reactivex.Scheduler;
import timber.log.Timber;

import static com.diplomski.injection.module.ThreadingModule.OBSERVE_SCHEDULER;
import static com.diplomski.injection.module.ThreadingModule.SUBSCRIBE_SCHEDULER;


public final class HomePresenterImpl extends BasePresenter implements HomePresenter {

    private HomeView view;

    private final MovieUseCase movieUseCase;

    private final Scheduler subscribeScheduler;

    private final Scheduler observeScheduler;

    private final MovieAPIConverter movieAPIConverter;

    private final StringManager stringManager;

    public HomePresenterImpl(@Named(SUBSCRIBE_SCHEDULER) final Scheduler subscribeScheduler,
                             @Named(OBSERVE_SCHEDULER) final Scheduler observeScheduler, final MovieUseCase movieUseCase,
                             final MovieAPIConverter movieAPIConverter, final StringManager stringManager) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.movieUseCase = movieUseCase;
        this.movieAPIConverter = movieAPIConverter;
        this.stringManager = stringManager;
    }

    @Override
    public void setView(final HomeView view) {
        this.view = view;
    }

    @Override
    public void getMovieInfo() {
        if (view != null) {
            addDisposable(movieUseCase.getMovieInfo()
                                      .map(movieAPIConverter::convertToMovieInfo)
                                      .subscribeOn(subscribeScheduler)
                                      .observeOn(observeScheduler)
                                      .subscribe(this::onGetMovieInfoSuccess, this::onGetMovieInfoFailure));
        }
    }

    private void onGetMovieInfoFailure(final Throwable throwable) {
        Timber.e(stringManager.getString(R.string.fetch_movie_info_error), throwable);
    }

    private void onGetMovieInfoSuccess(final List<MovieInfo> movieInfo) {
        if (view != null) {
            view.showData(movieInfo);
        }
    }
}
