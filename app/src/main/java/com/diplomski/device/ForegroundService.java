package com.diplomski.device;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;

import com.diplomski.R;
import com.diplomski.ui.home.HomeActivity;
import com.diplomski.util.Constants;
import com.google.android.things.userdriver.UserDriverManager;
import com.google.android.things.userdriver.location.GnssDriver;

import static com.diplomski.util.Constants.NOTIFICATION_CHANNEL.ANDROID_CHANNEL_ID;
import static com.diplomski.util.Constants.NOTIFICATION_CHANNEL.ANDROID_CHANNEL_NAME;

public class ForegroundService extends Service {

    private Handler trainingHandler = new Handler();
    //    private Handler locationHandler = new Handler();
    private Intent intent;
    //    private Intent intentLocation;
    private long startTime = 0L;
    private long timeInMilliseconds = 0L;
    private boolean stopTimer = false;
//    private LocationProvider fusedLocationProvider = null;
//    private Location currentLocation = null;
//    private Location oldLocation = null;
//    private double distance = 0;
//    private double distanceLastTwo = 0;
//    private float speed;
//    private float speedAccuracy;
//    private float speedInKmh;

    private GnssDriver mDriver;


    @Override
    public void onCreate() {
        super.onCreate();
        intent = new Intent(Constants.ACTION.BROADCAST_ACTION);
        // Create a new driver implementation
        mDriver = new GnssDriver();

        // Register with the framework
        UserDriverManager manager = UserDriverManager.getInstance();
        manager.registerGnssDriver(mDriver);
//        intentLocation = new Intent(Constants.ACTION.BROADCAST_ACTION_LOCATION);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getAction().equals(Constants.ACTION.STARTFOREGROUND_ACTION)) {

//            fusedLocationProvider = new LocationProvider(getApplicationContext());
//            fusedLocationProvider.startLocationUpdates();
            startTime = SystemClock.uptimeMillis();

            trainingHandler.postDelayed(updateTimerThread, 0);

//            locationHandler.postDelayed(updateLocationThread, 500);

            Intent notificationIntent = new Intent(this, HomeActivity.class);

            notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);

            notificationIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            NotificationManager mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);


            NotificationChannel androidChannel = new NotificationChannel(ANDROID_CHANNEL_ID,
                    ANDROID_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            androidChannel.enableLights(true);
            androidChannel.enableVibration(true);
            androidChannel.setLightColor(Color.GREEN);
            androidChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            mManager.createNotificationChannel(androidChannel);


            Notification notification = new Notification.Builder(getApplicationContext(), ANDROID_CHANNEL_ID)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText("Snimanje u tijeku")
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true)
                    .build();

            startForeground(Constants.NOTIFICATION_ID.FOREGROUND_SERVICE, notification);


        } else if (intent.getAction().equals(Constants.ACTION.STOPFOREGROUND_ACTION)) {
//            if (fusedLocationProvider != null) {
//                fusedLocationProvider.stopLocationUpdates();
//            }

//            currentLocation = null;
//            oldLocation = null;
//            distance = 0;
//            speed = 0;
//            speedAccuracy = 0;
//            speedInKmh = 0;
            timeInMilliseconds = 0L;
//            distanceLastTwo = 0;
            startTime = 0L;
            stopTimer = true;
            stopForeground(true);
            stopSelf();
        }
        return START_STICKY;
    }

    private Runnable updateTimerThread = new Runnable() {
        public void run() {
            timeInMilliseconds = SystemClock.uptimeMillis() - startTime;
            intent.putExtra("timeInMilliseconds", timeInMilliseconds);
//            Location result = new Location(LocationManager.GPS_PROVIDER);
//
//            mDriver.reportLocation(result);
//            Log.e("LOCA", result.getLongitude() + " " + result.getLatitude() + "");
            /*intent.putExtra("distanceInMeters",distance);
            intent.putExtra("elevationGainInMeters",elevationGain);
            intent.putExtra("calories", calories);*/
            sendBroadcast(intent);
            if (!stopTimer) {
                trainingHandler.postDelayed(this, 500);
            }
        }
    };

//    int i = 0;
//    private Runnable updateLocationThread = new Runnable() {
//        public void run() {
//            oldLocation = currentLocation;
    //if (i == 0) {
//            currentLocation = fusedLocationProvider.getCurrentLocation();
            /*} else if (i == 1) {
                currentLocation = new Location("");
                currentLocation.setLatitude(45.595882);
                currentLocation.setLongitude(17.212714);
            } else if (i == 2) {
                currentLocation = new Location("");
                currentLocation.setLatitude(45.595243);
                currentLocation.setLongitude(17.213698);
            } else if (i == 3) {
                currentLocation = new Location("");
                currentLocation.setLatitude(45.594540);
                currentLocation.setLongitude(17.213863);
            } else if (i == 4) {
                currentLocation = new Location("");
                currentLocation.setLatitude(45.594510);
                currentLocation.setLongitude(17.21489);
            } else if (i == 5) {
                currentLocation = new Location("");
                currentLocation.setLatitude(45.594587);
                currentLocation.setLongitude(17.217351);
            }*/
//            i++;
//            if (oldLocation != null && currentLocation != null) {
//                speed = currentLocation.getSpeed();
    //TODO check this
                /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    speedAccuracy = currentLocation.getSpeedAccuracyMetersPerSecond();
                }*/
//                speedInKmh = speed * 3.6F;
//                distance += fusedLocationProvider.calculateDistance(oldLocation, currentLocation);
//                distanceLastTwo = fusedLocationProvider.calculateDistance(oldLocation, currentLocation);
//                intentLocation.putExtra("newDistanceLastTwo", distanceLastTwo);
//                intentLocation.putExtra("newDistance", distance);
//                intentLocation.putExtra("newSpeed", speedInKmh);
//
//            }
//            intentLocation.putExtra("newLocation", currentLocation);
//
//            sendBroadcast(intentLocation);
//            if (!stopTimer) {
//                locationHandler.postDelayed(this, 5000);
//            }
//        }
//    };

    @Override
    public void onDestroy() {

        trainingHandler.removeCallbacks(updateTimerThread);
//        locationHandler.removeCallbacks(updateLocationThread);
//        if (fusedLocationProvider != null) {
//            fusedLocationProvider.stopLocationUpdates();
//        }

        UserDriverManager manager = UserDriverManager.getInstance();
        manager.unregisterGnssDriver();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}