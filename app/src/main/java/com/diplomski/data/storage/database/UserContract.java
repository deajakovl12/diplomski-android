package com.diplomski.data.storage.database;


public class UserContract {

    public static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserContract.UserEntry.TABLE_NAME + " (" +
                    UserContract.UserEntry.ID_USER + " INTEGER PRIMARY KEY," +
                    UserContract.UserEntry.USER_NAME + " TEXT," +
                    UserContract.UserEntry.USER_LAST_NAME + " TEXT," +
                    UserContract.UserEntry.USER_ADDRESS + " TEXT, " +
                    UserContract.UserEntry.USER_USERNAME + " TEXT, " +
                    UserContract.UserEntry.USER_POCETNA_KAZNA + " REAL, " +
                    UserContract.UserEntry.USER_PREOSTALO_KAZNE + " REAL, " +
                    UserContract.UserEntry.USER_IS_ADMIN + " INTEGER)";

    public static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserContract.UserEntry.TABLE_NAME;

    private UserContract() {
    }

    public static class UserEntry {
        public static final String TABLE_NAME = "logged_user";
        public static final String ID_USER = "logged_user_id";
        public static final String USER_NAME = "user_name";
        public static final String USER_LAST_NAME = "user_last_name";
        public static final String USER_ADDRESS = "user_address";
        public static final String USER_USERNAME = "user_username";
        public static final String USER_IS_ADMIN = "user_is_admin";
        public static final String USER_POCETNA_KAZNA = "user_pocetna_kazna";
        public static final String USER_PREOSTALO_KAZNE = "user_preostalo_kazne";

    }
}
