package com.diplomski.util;


public class Constants {
    public interface ACTION {
        public static String MAIN_ACTION = "foi.hr.calorietrek.action.main";
        public static String PLAY_ACTION = "foi.hr.calorietrek.action.play";
        public static String PAUSE_ACTION = "foi.hr.calorietrek.action.pause";
        public static String BROADCAST_ACTION = "dean.diplomski.action.broadcast";
        public static String STARTFOREGROUND_ACTION = "foi.hr.calorietrek.action.startforeground";
        public static String STOPFOREGROUND_ACTION = "foi.hr.calorietrek.action.stopforeground";

    }

    public interface GPSPARAMETERS {
        public static int UPDATE_INTERVAL = 7500;
        public static int FASTEST_UPDATE_INTERVAL = 3750;
        public  static  int ACCURACY = 100;
    }

    public interface NOTIFICATION_ID {
        public static int FOREGROUND_SERVICE = 101;

    }

    public interface NOTIFICATION_CHANNEL{

        public static final String ANDROID_CHANNEL_ID = "com.diplomski.ANDROID";
        public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    }
}
