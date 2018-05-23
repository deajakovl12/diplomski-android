package com.diplomski.domain.usecase;




import com.diplomski.data.api.models.request.FullRecordInfoRequest;
import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;
import com.facebook.stetho.inspector.protocol.module.Network;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Response;

public interface RecordUseCase {

    Single<FullRecordingInfo> getFullRecordInfo();

    Single<Boolean> checkIfDataUploadNeeded();

    Completable addNewRecord(RecordInfo recordInfo, double distance);

    Completable addFullRecord(FullRecordingInfo fullRecordingInfo);

    Single<Response<Void>> uploadRecordsToServer(List<FullRecordInfoRequest> fullRecordInfoRequests);

    Single<List<FullRecordInfoRequest>>  getAllRecordsThatNeedUpload();
}
