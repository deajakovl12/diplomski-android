package com.diplomski.util;


public class Constants {
    public interface ACTION {
        String MAIN_ACTION = "diplomski.action.main";
        String BROADCAST_ACTION = "diplomski.action.broadcast";
        String BROADCAST_ACTION_LOCATION = "fdiplomski.action.broadcast.location";
        String STARTFOREGROUND_ACTION = "diplomski.action.startforeground";
        String STOPFOREGROUND_ACTION = "diplomski.action.stopforeground";
        String LOCATION_CAME_ACTION = "diplomski.action.locationcame";

    }

    public interface NOTIFICATION_ID {
        int FOREGROUND_SERVICE = 101;
    }

    public interface GPSPARAMETERS {
        //regular update interval in miliseconds in which time will fused location provider client request location update
        int UPDATE_INTERVAL = 7500;
        //if fused location provider client gets new good location in less time than regular interval   ukoliko se "ispravna" lokacija ukaže i prije regularnog intervala aplikacija fused location provider client će postaviti novu lokaciju i prije regularnog intervala
        int FASTEST_UPDATE_INTERVAL = 3750;
        //accuracy of GPS locations that device receives, also affects battery life parameters can be  high-100, balanced-102,  low-104, noPower-105
        int ACCURACY = 100;
        //world record in 100m run meters/second
        double FASTEST_HUMAN_SPEED = 12.4222;
        //average human fastest running speed (debatable) but around  16.09 to 24.14 km/h, taken from higher value in m/s
        double AVERAGE_HUMAN_FASTEST_RUNNING_SPEED = 6.7056;
        //GPS-enabled smartphones are typically accurate to within a 4.9 m
        double GPS_ERROR_RANGE = 4.9;
        //max cargo weight for human to be able to run at speeds between AVERAGE and FASTEST human speed
        int MAX_CARGO_FOR_FAST_RUN = 5;
        int TWO_MINUTES = 1000 * 60 * 2;
    }


    public interface NOTIFICATION_CHANNEL{

        public static final String ANDROID_CHANNEL_ID = "com.diplomski.ANDROID";
        public static final String ANDROID_CHANNEL_NAME = "ANDROID CHANNEL";
    }
}
