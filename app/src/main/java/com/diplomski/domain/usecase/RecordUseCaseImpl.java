package com.diplomski.domain.usecase;




import com.diplomski.data.api.models.request.FullRecordInfoRequest;
import com.diplomski.data.service.NetworkService;
import com.diplomski.data.storage.TemplatePreferences;
import com.diplomski.data.storage.database.DatabaseHelper;
import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Response;

public class RecordUseCaseImpl implements RecordUseCase{

    private final NetworkService networkService;

    private final TemplatePreferences preferences;

    private final DatabaseHelper databaseHelper;


    public RecordUseCaseImpl(NetworkService networkService,
                             TemplatePreferences preferences,
                             DatabaseHelper databaseHelper) {
        this.networkService = networkService;
        this.preferences = preferences;
        this.databaseHelper = databaseHelper;
    }


    @Override
    public Single<FullRecordingInfo> getFullRecordInfo() {
        return null;
    }

    @Override
    public Completable addNewRecord(RecordInfo recordInfo, double distance) {
        return Completable.defer(() -> databaseHelper.addNewRecord(recordInfo, distance));
    }

    @Override
    public Completable addFullRecord(FullRecordingInfo fullRecordingInfo) {
        return Completable.defer(() -> databaseHelper.addFullRecord(fullRecordingInfo));
    }

    @Override
    public Single<Boolean> checkIfDataUploadNeeded() {
        return Single.defer(databaseHelper::checkIfDataUploadNeeded);
    }

    @Override
    public Single<Response<Void>> uploadRecordsToServer(List<FullRecordInfoRequest> fullRecordInfoRequests) {
        return Single.defer(() -> networkService.uploadRecordsToServer(fullRecordInfoRequests));
    }

    @Override
    public Single<List<FullRecordInfoRequest>> getAllRecordsThatNeedUpload() {
        return Single.defer(databaseHelper::getAllRecordsThatNeedUpload);
    }
}
