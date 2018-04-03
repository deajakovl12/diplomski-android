package com.diplomski.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.util.MutableShort;
import android.widget.Toast;

import com.diplomski.R;
import com.diplomski.domain.model.MovieInfo;
import com.diplomski.injection.component.ActivityComponent;
import com.diplomski.ui.base.activities.BaseActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class HomeActivity extends BaseActivity implements HomeView {

    @Inject
    HomePresenter presenter;

    WifiManager mWifiManager;

    public static Intent createIntent(final Context context) {
        return new Intent(context, HomeActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        mWifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        registerReceiver(mWifiScanReceiver,
                new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        mWifiManager.startScan();

        locationTest();
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

    @OnClick(R.id.button_test)
    public void call(){
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

    private final BroadcastReceiver mWifiScanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context c, Intent intent) {
            if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
                List<ScanResult> mScanResults = mWifiManager.getScanResults();
                // add your logic here
                for (ScanResult mScanResult : mScanResults) {
                    Timber.e(mScanResult.SSID);
                }
            }
        }
    };

    private void locationTest(){
        boolean gps_enabled = false;
        boolean network_enabled = false;

        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Location net_loc = null, gps_loc = null, finalLoc = null;

        if (gps_enabled)
            gps_loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (network_enabled)
            net_loc = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

        if (gps_loc != null && net_loc != null) {

            //smaller the number more accurate result will
            if (gps_loc.getAccuracy() > net_loc.getAccuracy())
                finalLoc = net_loc;
            else
                finalLoc = gps_loc;

            // I used this just to get an idea (if both avail, its upto you which you want to take as I've taken location with more accuracy)

        } else {

            if (gps_loc != null) {
                finalLoc = gps_loc;
            } else if (net_loc != null) {
                finalLoc = net_loc;
            }
        }

        Timber.e("AAAAA" + finalLoc.getLatitude() + " " +  finalLoc.getLongitude());
    }
}
