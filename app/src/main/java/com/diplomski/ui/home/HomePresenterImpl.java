package com.diplomski.ui.home;


import com.diplomski.R;
import com.diplomski.data.api.converter.MovieAPIConverter;
import com.diplomski.data.api.models.request.FullRecordInfoRequest;
import com.diplomski.data.api.models.response.MovieApiResponse;
import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;
import com.diplomski.domain.usecase.MovieUseCase;
import com.diplomski.domain.usecase.RecordUseCase;
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

    private final RecordUseCase recordUseCase;

    public HomePresenterImpl(@Named(SUBSCRIBE_SCHEDULER) final Scheduler subscribeScheduler,
                             @Named(OBSERVE_SCHEDULER) final Scheduler observeScheduler, final MovieUseCase movieUseCase,
                             final MovieAPIConverter movieAPIConverter, final StringManager stringManager,
                             final RecordUseCase recordUseCase) {
        this.subscribeScheduler = subscribeScheduler;
        this.observeScheduler = observeScheduler;
        this.movieUseCase = movieUseCase;
        this.movieAPIConverter = movieAPIConverter;
        this.stringManager = stringManager;
        this.recordUseCase = recordUseCase;
    }

    @Override
    public void setView(final HomeView view) {
        this.view = view;
    }

    @Override
    public void getMovieInfo() {
        if (view != null) {
            addDisposable(movieUseCase.getMovieInfo()
                    .subscribeOn(subscribeScheduler)
                    .observeOn(observeScheduler)
                    .subscribe(this::onGetMovieInfoSuccess, this::onGetMovieInfoFailure));
        }
    }

    private void onGetMovieInfoSuccess(MovieApiResponse movieApiResponses) {
        if (view != null) {
            view.showData(movieApiResponses);
        }
    }

    private void onGetMovieInfoFailure(final Throwable throwable) {
        Timber.e(stringManager.getString(R.string.fetch_movie_info_error), throwable);
    }

    private void onGetMovieInfoSuccess(final List<MovieApiResponse> movieInfo) {
        if (view != null) {
            // view.showData(movieInfo);
        }
    }

    @Override
    public void saveFullRecordToDb(FullRecordingInfo fullRecordingInfo) {
        addDisposable(recordUseCase.addFullRecord(fullRecordingInfo)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(this::onAddNewFullRecordSuccess, this::onNewFullRecordFailure));
    }

    private void onNewFullRecordFailure(Throwable throwable) {
        Timber.e(throwable.getMessage());

    }

    private void onAddNewFullRecordSuccess() {
        if (view != null) {
            view.recordingStarted();
        }
    }

    @Override
    public void saveRecordToDb(RecordInfo recordInfo, double distance) {
        addDisposable(recordUseCase.addNewRecord(recordInfo, distance)
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(this::onAddNewRecordSuccess, this::onNewRecordFailure));
    }

    private void onAddNewRecordSuccess() {
        Timber.e("record added");
    }


    private void onNewRecordFailure(Throwable throwable) {
        Timber.e(throwable.getMessage());
    }

    @Override
    public void checkDataForUpload() {
        addDisposable(recordUseCase.checkIfDataUploadNeeded()
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(this::onCheckSuccess, this::onCheckFailure));
    }

    private void onCheckSuccess(Boolean notSent) {
        if (view != null) {
            view.needDataUpload(notSent);
        }
    }

    private void onCheckFailure(Throwable throwable) {
        Timber.e(stringManager.getString(R.string.fetch_check_error), throwable.getMessage());
    }

    @Override
    public void uploadRecordsToServer() {
        addDisposable(recordUseCase.getAllRecordsThatNeedUpload()
                .subscribeOn(subscribeScheduler)
                .observeOn(observeScheduler)
                .subscribe(this::onGetAllRecordsSuccess, this::onGetAllRecordsFailure));
    }

    private void onGetAllRecordsFailure(Throwable throwable) {
        Timber.e(stringManager.getString(R.string.fetch_all_record_info_erorr) + throwable.getMessage());

    }

    private void onGetAllRecordsSuccess(List<FullRecordInfoRequest> fullRecordInfoRequests) {
        //TODO HERE UPLOAD THIS DATA TO SERVER AND AFTER IT IS UPLOADED CHANGE ALL SERVER UPLOAD TO 2!
        for (FullRecordInfoRequest fullRecordInfoRequest : fullRecordInfoRequests) {
            for (FullRecordInfoRequest.OneRecordInfoRequest oneRecordInfoRequest : fullRecordInfoRequest.oneRecordList) {
                Timber.e(oneRecordInfoRequest.idFullrecord + "----------" + oneRecordInfoRequest.oneRecordId + "++++++" + oneRecordInfoRequest.distanceFromLast);
            }
        }
    }
}
