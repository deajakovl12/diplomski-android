package com.diplomski.data.storage.database;


public class FullRecordContract {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FullRecordEntry.TABLE_NAME + " (" +
                    FullRecordEntry.ID_FULL_RECORD + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    FullRecordEntry.ID_USER + " TEXT," +
                    FullRecordEntry.ID_FULL_RECORD_ID_DATE + " TEXT," +
                    FullRecordEntry.START_DATE + " TEXT," +
                    FullRecordEntry.DISTANCE_TRAVELLED + " REAL, " +
                    FullRecordEntry.SENT_TO_SERVER + " INTEGER, " +
                    FullRecordEntry.IMAGE + " STRING, " +
                    FullRecordEntry.SIGNATURE + " STRING)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FullRecordEntry.TABLE_NAME;

    private FullRecordContract() {
    }

    public static class FullRecordEntry {
        public static final String TABLE_NAME = "full_record";
        public static final String ID_USER = "user_id";
        public static final String ID_FULL_RECORD = "full_record_id";
        public static final String ID_FULL_RECORD_ID_DATE = "full_record_id_date";
        public static final String START_DATE = "start_date";
        public static final String SENT_TO_SERVER = "sent_to_server"; //1 NOT SENT , 2 SENT TO SERVER
        public static final String DISTANCE_TRAVELLED = "distance";
        public static final String IMAGE = "image";
        public static final String SIGNATURE = "signature";

    }
}
