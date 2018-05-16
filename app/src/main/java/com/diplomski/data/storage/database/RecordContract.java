package com.diplomski.data.storage.database;


public class RecordContract {
    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + RecordEntry.TABLE_NAME + " (" +
                    RecordEntry.ID_RECORD + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    RecordEntry.ID_FULL_RECORD + " TEXT," +
                    RecordEntry.LAT + " REAL," +
                    RecordEntry.LNG + " REAL, " +
                    RecordEntry.SPEED + " REAL, " +
                    RecordEntry.SPEED_LIMIT + " REAL, " +
                    RecordEntry.CURRENT_DATE + " TEXT, " +
                    RecordEntry.DISTANCE_FROM_LAST_LOCATION + " REAL)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + RecordEntry.TABLE_NAME;

    private RecordContract() {
    }

    public static class RecordEntry {
        public static final String TABLE_NAME = "one_record";
        public static final String ID_RECORD = "record_id";
        public static final String ID_FULL_RECORD = "full_record_id";
        public static final String LAT = "latitude";
        public static final String LNG = "longitude";
        public static final String SPEED = "speed";
        public static final String SPEED_LIMIT = "speed_limit";
        public static final String CURRENT_DATE = "current_date";
        public static final String DISTANCE_FROM_LAST_LOCATION = "distance_from_last";

    }
}
