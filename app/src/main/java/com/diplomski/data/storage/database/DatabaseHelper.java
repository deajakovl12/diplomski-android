package com.diplomski.data.storage.database;




import com.diplomski.data.api.models.request.FullRecordInfoRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.Single;
import io.reactivex.SingleSource;

public interface DatabaseHelper {

    Single<LoginApiResponse>  loginUser(LoginApiResponse user);

    Single<LoginApiResponse> getLoggedInUser();

    Single<Boolean> checkIfDataUploadNeeded();

    Completable addNewRecord(RecordInfo recordInfo, double distance);

    Completable addFullRecord(FullRecordingInfo fullRecordingInfo);

    SingleSource<List<FullRecordInfoRequest>> getAllRecordsThatNeedUpload();

    Single<Boolean> updateRecordsSentToServer(double distanceTraveled);

    Completable removeAllDataFromDb();
}
