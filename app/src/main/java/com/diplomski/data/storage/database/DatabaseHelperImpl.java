package com.diplomski.data.storage.database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.diplomski.data.api.models.request.FullRecordInfoRequest;
import com.diplomski.data.storage.PreferenceRepository;
import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.SingleSource;
import timber.log.Timber;

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
    public Single<Boolean> checkIfDataUploadNeeded() {
        return Single.defer(() -> {
            SQLiteDatabase db = getReadableDatabase();
            String[] projection = {
                    FullRecordContract.FullRecordEntry.SENT_TO_SERVER
            };
            Cursor cursor;
            cursor = db.query(
                    FullRecordContract.FullRecordEntry.TABLE_NAME, projection, FullRecordContract.FullRecordEntry.SENT_TO_SERVER + "=1", null, null, null, null);

            boolean notSent = false;
            while (cursor.moveToNext()) {
                notSent = true;
            }
            cursor.close();
            return Single.just(notSent);
        });
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

    @Override
    public SingleSource<List<FullRecordInfoRequest>> getAllRecordsThatNeedUpload() {
        return Single.defer(() -> {

            SQLiteDatabase db = getReadableDatabase();
            String[] projection = {
                    FullRecordContract.FullRecordEntry.ID_FULL_RECORD_ID_DATE,
                    FullRecordContract.FullRecordEntry.ID_USER,
                    FullRecordContract.FullRecordEntry.START_DATE,
                    FullRecordContract.FullRecordEntry.DISTANCE_TRAVELLED,
                    FullRecordContract.FullRecordEntry.IMAGE,
                    FullRecordContract.FullRecordEntry.SIGNATURE
            };
            Cursor cursor;
            cursor = db.query(
                    FullRecordContract.FullRecordEntry.TABLE_NAME, projection, FullRecordContract.FullRecordEntry.SENT_TO_SERVER + "=1", null, null, null, null);

            List<FullRecordInfoRequest> fullRecordInfoRequestList = new ArrayList<>();
            while (cursor.moveToNext()) {
                FullRecordInfoRequest fullRecordInfoRequest = new FullRecordInfoRequest(
                        cursor.getString(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getDouble(3),
                        cursor.getString(4),
                        cursor.getString(5)
                );
                fullRecordInfoRequestList.add(fullRecordInfoRequest);
            }
            cursor.close();

            String[] projectionOneRecord = {
                    RecordContract.RecordEntry.ID_FULL_RECORD,
                    RecordContract.RecordEntry.LAT,
                    RecordContract.RecordEntry.LNG,
                    RecordContract.RecordEntry.SPEED,
                    RecordContract.RecordEntry.SPEED_LIMIT,
                    RecordContract.RecordEntry.DISTANCE_FROM_LAST_LOCATION,
                    RecordContract.RecordEntry.CURRENT_DATE,
                    RecordContract.RecordEntry.ID_RECORD
            };
            for (FullRecordInfoRequest fullRecordInfoRequest : fullRecordInfoRequestList) {
                Cursor cursorOneRecord = db.query(
                        RecordContract.RecordEntry.TABLE_NAME, projectionOneRecord, RecordContract.RecordEntry.ID_FULL_RECORD + "=?", new String[]{fullRecordInfoRequest.fullRecordIdDB}, null, null, null);
                while (cursorOneRecord.moveToNext()) {
                    FullRecordInfoRequest.OneRecordInfoRequest oneRecordInfoRequest =
                            new FullRecordInfoRequest.OneRecordInfoRequest(
                                    cursorOneRecord.getString(0),
                                    cursorOneRecord.getDouble(1),
                                    cursorOneRecord.getDouble(2),
                                    cursorOneRecord.getDouble(3),
                                    cursorOneRecord.getDouble(4),
                                    cursorOneRecord.getString(6),
                                    cursorOneRecord.getDouble(5),
                                    cursorOneRecord.getInt(7)
                            );
                    fullRecordInfoRequest.addOneRecordToList(oneRecordInfoRequest);
                }
                cursorOneRecord.close();
            }

            return Single.just(fullRecordInfoRequestList);
        });
    }
}
