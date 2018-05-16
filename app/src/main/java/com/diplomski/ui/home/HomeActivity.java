package com.diplomski.ui.home;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.GnssStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.diplomski.R;
import com.diplomski.domain.model.MovieInfo;
import com.diplomski.injection.component.ActivityComponent;
import com.diplomski.ui.base.activities.BaseActivity;
import com.google.android.things.contrib.driver.gps.NmeaGpsDriver;

import java.io.IOException;
import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class HomeActivity extends BaseActivity implements HomeView {

    private static final String TAG = "GpsActivity";

    public static final int UART_BAUD = 9600;
    public static final float ACCURACY = 2.5f; // From GPS datasheet

    private LocationManager mLocationManager;
    private NmeaGpsDriver mGpsDriver;


    @Inject
    HomePresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        // We need permission to get location updates
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // A problem occurred auto-granting the permission
            Log.d(TAG, "No permission");
            return;
        }

        try {
            // Register the GPS driver
            mGpsDriver = new NmeaGpsDriver(this, "UART6",
                    UART_BAUD, ACCURACY);
            mGpsDriver.register();
            // Register for location updates
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    0, 0, mLocationListener);
            mLocationManager.registerGnssStatusCallback(mStatusCallback);
            mLocationManager.addNmeaListener(mMessageListener);
        } catch (IOException e) {
            Log.w(TAG, "Unable to open GPS UART", e);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        presenter.getMovieInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.dispose();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // Verify permission was granted
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "No permission");
            return;
        }

        if (mGpsDriver != null) {
            // Unregister components
            mGpsDriver.unregister();
            mLocationManager.removeUpdates(mLocationListener);
            mLocationManager.unregisterGnssStatusCallback(mStatusCallback);
            mLocationManager.removeNmeaListener(mMessageListener);
            try {
                mGpsDriver.close();
            } catch (IOException e) {
                Log.w(TAG, "Unable to close GPS driver", e);
            }
        }
    }

    @OnClick(R.id.button_test)
    public void call() {
    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public void showData(final List<MovieInfo> movieInfo) {
        Toast.makeText(this, movieInfo.get(0).getTitle(), Toast.LENGTH_SHORT).show();
        Log.e("TASK", "AA" + movieInfo.get(0).getTitle());
    }

    /** Report location updates */
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG, "Location update: " + location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) { }

        @Override
        public void onProviderEnabled(String provider) { }

        @Override
        public void onProviderDisabled(String provider) { }
    };

    /** Report satellite status */
    private GnssStatus.Callback mStatusCallback = new GnssStatus.Callback() {
        @Override
        public void onStarted() { }

        @Override
        public void onStopped() { }

        @Override
        public void onFirstFix(int ttffMillis) { }

        @Override
        public void onSatelliteStatusChanged(GnssStatus status) {
            Log.v(TAG, "GNSS Status: " + status.getSatelliteCount() + " satellites.");
        }
    };

    /** Report raw NMEA messages */
    private OnNmeaMessageListener mMessageListener = new OnNmeaMessageListener() {
        @Override
        public void onNmeaMessage(String message, long timestamp) {
            Log.v(TAG, "NMEA: " + message);
        }
    };


}
