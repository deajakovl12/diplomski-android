package com.diplomski.domain.usecase;




import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;

import io.reactivex.Completable;
import io.reactivex.Single;

public interface RecordUseCase {

    Single<FullRecordingInfo> getFullRecordInfo();

    Completable addNewRecord(RecordInfo recordInfo, double distance);

    Completable addFullRecord(FullRecordingInfo fullRecordingInfo);

}
