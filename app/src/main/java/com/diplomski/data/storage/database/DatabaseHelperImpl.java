package com.diplomski.data.storage.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.diplomski.data.storage.PreferenceRepository;
import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;

import io.reactivex.Completable;
import io.reactivex.Single;

public class DatabaseHelperImpl extends SQLiteOpenHelper implements DatabaseHelper {

    protected Context context;
    private PreferenceRepository preferenceRepository;

    public DatabaseHelperImpl(final Context context, final String databaseName, final int databaseVersion,
                              final PreferenceRepository preferenceRepository) {
        super(context, databaseName, null, databaseVersion);
        this.context = context;
        this.preferenceRepository = preferenceRepository;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(FullRecordContract.SQL_CREATE_ENTRIES);
        db.execSQL(RecordContract.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL(FullRecordContract.SQL_DELETE_ENTRIES);
        db.execSQL(RecordContract.SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    @Override
    public Completable addFullRecord(FullRecordingInfo fullRecordingInfo) {
        return Completable.defer(() -> {
            ContentValues values = new ContentValues();
            SQLiteDatabase db = getReadableDatabase();

            values.put(FullRecordContract.FullRecordEntry.ID_USER, fullRecordingInfo.userId);
            values.put(FullRecordContract.FullRecordEntry.START_DATE, fullRecordingInfo.dateStart);
            values.put(FullRecordContract.FullRecordEntry.SENT_TO_SERVER, fullRecordingInfo.sentToServer);
            values.put(FullRecordContract.FullRecordEntry.DISTANCE_TRAVELLED, fullRecordingInfo.distanceTraveled);
            values.put(FullRecordContract.FullRecordEntry.IMAGE, fullRecordingInfo.image);
            values.put(FullRecordContract.FullRecordEntry.SIGNATURE, fullRecordingInfo.signature);

            if (preferenceRepository.getLastRecordId().equals("")) {
                values.put(FullRecordContract.FullRecordEntry.ID_FULL_RECORD_ID_DATE, "1-" + fullRecordingInfo.dateStart);
            } else {
                String array[] = preferenceRepository.getLastRecordId().split("-");
                int brojZadnjeg = Integer.parseInt(array[0]) + 1;
                values.put(FullRecordContract.FullRecordEntry.ID_FULL_RECORD_ID_DATE, brojZadnjeg + "-" + fullRecordingInfo.dateStart);

            }

            long rowId = db.insert(FullRecordContract.FullRecordEntry.TABLE_NAME, null, values);
            if (rowId != -1) {
                preferenceRepository.setLastRecordId(rowId + "-" + fullRecordingInfo.dateStart);
            }
            return Completable.complete();
        });
    }

    @Override
    public Single<FullRecordingInfo> getFullRecordInfo() {
        return null;
    }

    @Override
    public Completable addNewRecord(RecordInfo recordInfo, double distance) {
        return Completable.defer(() -> {
            ContentValues values = new ContentValues();
            SQLiteDatabase db = getReadableDatabase();

            values.put(RecordContract.RecordEntry.ID_FULL_RECORD, preferenceRepository.getLastRecordId());
            values.put(RecordContract.RecordEntry.LAT, recordInfo.lat);
            values.put(RecordContract.RecordEntry.LNG, recordInfo.lng);
            values.put(RecordContract.RecordEntry.SPEED, recordInfo.speed);
            values.put(RecordContract.RecordEntry.SPEED_LIMIT, recordInfo.speedLimit);
            values.put(RecordContract.RecordEntry.DISTANCE_FROM_LAST_LOCATION, recordInfo.distanceFromLast);
            values.put(RecordContract.RecordEntry.CURRENT_DATE, recordInfo.currentDate);

            db.insert(RecordContract.RecordEntry.TABLE_NAME, null, values);

            values = new ContentValues();
            values.put(FullRecordContract.FullRecordEntry.DISTANCE_TRAVELLED, distance);

            String selection = FullRecordContract.FullRecordEntry.ID_FULL_RECORD_ID_DATE + "=?";
            String[] selectionArgs = {String.valueOf(preferenceRepository.getLastRecordId())};

            db.update(FullRecordContract.FullRecordEntry.TABLE_NAME, values, selection, selectionArgs);

            return Completable.complete();
        });
    }
}
