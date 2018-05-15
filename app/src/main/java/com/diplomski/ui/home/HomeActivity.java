package com.diplomski.ui.home;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.diplomski.R;
import com.diplomski.device.ForegroundService;
import com.diplomski.domain.model.MovieInfo;
import com.diplomski.injection.component.ActivityComponent;
import com.diplomski.ui.base.activities.BaseActivity;
import com.diplomski.util.Constants;
import com.google.android.things.contrib.driver.gps.GpsModuleCallback;
import com.google.android.things.contrib.driver.gps.NmeaGpsDriver;
import com.google.android.things.contrib.driver.gps.NmeaGpsModule;
import com.google.android.things.pio.PeripheralManager;
import com.google.android.things.pio.UartDevice;
import com.google.android.things.pio.UartDeviceCallback;
import com.google.android.things.userdriver.UserDriverManager;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;

import static android.content.ContentValues.TAG;


public class HomeActivity extends BaseActivity implements HomeView {

    @Inject
    HomePresenter presenter;

    WifiManager mWifiManager;

    BroadcastReceiver broadcastReceiver = null;

    @BindView(R.id.time_text)
    TextView textViewTime;

    //FusedLocationProviderClient mFusedLocationClient;
    //GoogleApiClient mgoogle = null;
    //LocationRequest mLocation;

    //private GpsDriver mDriver;
    // private GpsDriver mGpsDriver;
    private UserDriverManager manager;

    private UartDevice mDevice;

    // UART Configuration Parameters
    private static final int BAUD_RATE = 115200;
    private static final int DATA_BITS = 8;
    private static final int STOP_BITS = 1;

    private static final int CHUNK_SIZE = 512;

    private HandlerThread mInputThread;
    private Handler mInputHandler;

//    private LocationManager mLocationManager;
//    private NmeaGpsDriver mGpsDriver;

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

       /* mLocationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

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
        } catch (IOException e) {
            Log.w(TAG, "Unable to open GPS UART", e);
        }*/


        //mDriver = new GpsDriver();
        //manager = UserDriverManager.getInstance();
        //manager.registerGpsDriver(mDriver);

        //Location location = parseLocationFromString(rawGpsData);
        //mDriver.reportLocation(location);
    }

    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            Log.v(TAG, "Location update: " + location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.v(TAG, "Location update: " + "STATUS CHANGE");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.v(TAG, "Location update: " + "PROVIDER ENABLD");

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.v(TAG, "Location update: " + "PROVIDER DISABLED");

        }
    };

    public void readUartBuffer(UartDevice uart) throws IOException {
        Log.e("READ", "READ CALLED");
        final int maxCount = 1000;
        byte[] buffer = new byte[maxCount];

        int count;
        while ((count = uart.read(buffer, buffer.length)) > 0) {
            Log.e(TAG, "Read " + count + " bytes from peripheral");
        }
    }

    public void configureUartFrame(UartDevice uart) throws IOException {
        Log.e("FRAM RATE", "FRAM RATE CALLED");
        uart.setBaudrate(4800);
        uart.setDataSize(8);
        uart.setParity(UartDevice.PARITY_NONE);
        uart.setStopBits(1);
    }

    public void setFlowControlEnabled(UartDevice uart, boolean enable) throws IOException {
        if (enable) {
            // Enable hardware flow control
            uart.setHardwareFlowControl(UartDevice.HW_FLOW_CONTROL_AUTO_RTSCTS);
        } else {
            // Disable flow control
            uart.setHardwareFlowControl(UartDevice.HW_FLOW_CONTROL_NONE);
        }
    }

    private Location parseLocationFromString(String gpsData) {
        Location result = new Location(LocationManager.GPS_PROVIDER);

        Log.e("RESULT", gpsData + " a");

        //parse gpsData

        //required
        /*result.setAccuracy( getAccuracyFromGpsData( gpsData ) );
        result.setTime( getTimeFromGpsData( gpsData ) );
        result.setLatitude( getLatitudeFromGpsData( gpsData ) );
        result.setLongitude( getLongitudeFromGpsData( gpsData ) );

        //optional
        result.setAltitude( getAltitudeFromGpsData( gpsData ) );
        result.setBearing( getBearingFromGpsData( gpsData ) );
        result.setSpeed( getSpeedFromGpsData( gpsData ) );*/

        return result;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        // presenter.getMovieInfo();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.dispose();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private UartDeviceCallback mUartCallback = new UartDeviceCallback() {
        @Override
        public boolean onUartDeviceDataAvailable(UartDevice uart) {
            Log.e(TAG, "TRANSFER UART");
            transferUartData();
            return true;
        }

        @Override
        public void onUartDeviceError(UartDevice uart, int error) {
            Log.e(TAG, uart + ": Error event " + error);
        }
    };

    private Runnable mTransferUartRunnable = () -> transferUartData();

    private void transferUartData() {
        Log.e(TAG, "TRANSFER UART IN METHOD");
        if (mDevice != null) {
            // Loop until there is no more data in the RX buffer.
            try {
                byte[] buffer = new byte[CHUNK_SIZE];
                int read;
                Log.e(TAG, "TRANSFER UART READ");
                while ((read = mDevice.read(buffer, buffer.length)) > 0) {
//                    mLoopbackDevice.write(buffer, read);
                    Log.e("TAAAA", read + " ");
                }
                Log.e(TAG, "TRANSFER UART END READ");
            } catch (IOException e) {
                Log.e(TAG, "Unable to transfer data over UART");
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

//        if (mDevice != null) {
//            try {
//                mDevice.close();
//                mDevice = null;
//            } catch (IOException e) {
//                Log.w(TAG, "Unable to close UART device", e);
//            }
//        }


        Log.d(TAG, "Loopback Destroyed");

        // Terminate the worker thread
        if (mInputThread != null) {
            mInputThread.quitSafely();
        }

        // Attempt to close the UART device
        try {
            closeUart();
        } catch (IOException e) {
            Log.e(TAG, "Error closing UART device:", e);
        }
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
            Intent stopIntent = new Intent(HomeActivity.this, ForegroundService.class);
            stopIntent.setAction(Constants.ACTION.STOPFOREGROUND_ACTION);
            startService(stopIntent);
        }
    }

    private void closeUart() throws IOException {
        if (mDevice != null) {
            mDevice.unregisterUartDeviceCallback(mUartCallback);
            try {
                mDevice.close();
            } finally {
                mDevice = null;
            }
        }
    }

    NmeaGpsModule mGpsModule;
    NmeaGpsDriver mGpsDriver;

    private void openUart(String name, int baudRate) throws IOException {
        Log.e(TAG, "OPEN UART METHOD CALL");
        mDevice = PeripheralManager.getInstance().openUartDevice(name);
        // Configure the UART
        mDevice.setBaudrate(baudRate);
        mDevice.setDataSize(DATA_BITS);
        mDevice.setParity(UartDevice.PARITY_NONE);
        mDevice.setStopBits(STOP_BITS);

        mDevice.registerUartDeviceCallback(mInputHandler, mUartCallback);
    }

    @OnClick(R.id.button_test)
    public void call() {

        mInputThread = new HandlerThread("InputThread");
        mInputThread.start();
        mInputHandler = new Handler(mInputThread.getLooper());

        try {
            Log.e(TAG, "OPEN UART");
            openUart("UART6", BAUD_RATE);
            mInputHandler.post(mTransferUartRunnable);
        } catch (IOException e) {
            Log.e(TAG, "Unable to open UART device", e);
        }


//        PeripheralManager manager = PeripheralManager.getInstance();
//        List<String> deviceList = manager.getUartDeviceList();
//        if (deviceList.isEmpty()) {
//            Log.e("UART", "No UART port available on this device.");
//        } else {
//            Log.e("UART", "List of available devices: " + deviceList);
//            try {


//            } catch (IOException e) {
//                Log.e(TAG, "Unable to access UART device", e);
//            }

//            try {
//                mGpsModule = new NmeaGpsModule(
//                        deviceList.get(0),
//                        9600 // specified baud rate for your GPS peripheral
//                );
//                mGpsModule.setGpsAccuracy(100f); // specified accuracy for your GPS peripheral
//                mGpsModule.setGpsModuleCallback(new GpsModuleCallback() {
//                    @Override
//                    public void onGpsSatelliteStatus(boolean active, int satellites) {
//                        Log.e("onGpsSatelliteStatus", active + " " + satellites);
//                    }
//
//                    @Override
//                    public void onGpsTimeUpdate(long timestamp) {
//                        Log.e("onGpsTimeUpdate", timestamp + " ");
//
//                    }
//
//                    @Override
//                    public void onGpsPositionUpdate(long timestamp, double latitude, double longitude, double altitude) {
//                        Log.e("onGpsPositionUpdate", timestamp + " " + latitude + " " + longitude + " " + altitude);
//
//                    }
//
//                    @Override
//                    public void onGpsSpeedUpdate(float speed, float bearing) {
//                        Log.e("onGpsSpeedUpdate", speed + " " + bearing);
//                    }
//                });
//            } catch (IOException e) {
//                Log.e("EXCEPTION GPS", e.getMessage());
//            }

//            try {
//                mGpsDriver = new NmeaGpsDriver(
//                        this,           // context
//                        "UART6",
//                        115200,       // specified baud rate for your GPS peripheral
//                        100f        // specified accuracy for your GPS peripheral
//                );
//            } catch (IOException e) {
//                Log.e("EXCEPTION GPS", e.getMessage());
//            }
//
//            mGpsDriver.register();
//        }


        Intent startIntent = new Intent(HomeActivity.this, ForegroundService.class);
        startIntent.setAction(Constants.ACTION.STARTFOREGROUND_ACTION);
        startService(startIntent);
        registerBroadCastReceiver();
        //createLocationRequest();
    }

    @OnClick(R.id.stop_test)
    public void stop() {

//        if (mDevice != null) {
//            try {
//                mDevice.close();
//                mDevice = null;
//            } catch (IOException e) {
//                Log.w(TAG, "Unable to close UART device", e);
//            }
//        }
//        try {
//            mGpsModule.close();
//        } catch (IOException e) {
//             error closing gps module
//        }
        mGpsDriver.unregister();
        try {
            mGpsDriver.close();
        } catch (IOException e) {
            // error closing gps driver
        }
        if (broadcastReceiver != null) {
            unregisterReceiver(broadcastReceiver);
        }
        startService(new Intent(HomeActivity.this, ForegroundService.class).setAction(Constants.ACTION.STOPFOREGROUND_ACTION));
    }


    /*protected void createLocationRequest() {
        mLocation = LocationRequest.create();

        mLocation.setSmallestDisplacement(500); // 2km
        mLocation.setInterval(500);// 3 min
        mLocation.setFastestInterval(300);// 2 min
        mLocation.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (mgoogle == null) {
            mgoogle = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            mgoogle.connect();
        }
    }*/


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

    private void registerBroadCastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long timeInMilliseconds = intent.getLongExtra("timeInMilliseconds", 0);
                textViewTime.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMilliseconds),
                        TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timeInMilliseconds)),
                        TimeUnit.MILLISECONDS.toSeconds(timeInMilliseconds) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeInMilliseconds))));
                /*float distance= intent.getFloatExtra("distanceInMeters",0);
                txtDistance.setText(String.format("%.2f", distance)+"m");
                double elevationGain = intent.getDoubleExtra("elevationGainInMeters",0);
                txtElevation.setText(String.format("%.2f", elevationGain)+"m");
                double calorie = intent.getDoubleExtra("calories", 0);
                txtKcal.setText(String.format("%.1f", calorie));*/
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter(Constants.ACTION.BROADCAST_ACTION));
    }

    /*@Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("CONNECTED", "CONNECTED");
        /*Location lastKnownLocation = LocationServices.FusedLocationApi.getLastLocation(mgoogle);

        if (lastKnownLocation == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mgoogle, mLocation, this);
            Log.e("CONNECTED", "NULL");

        } else {
            Log.e("CONNECTED", lastKnownLocation.getLongitude() + " " + lastKnownLocation.getLatitude());

            Log.e("CONNECTED", "NULL");
        }*/
}

    /*@Override
    public void onConnectionSuspended(int i) {
        Log.e("SUSPENDED", "SUSPENDED");

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("FAILED", "FAILED");

    }

    @Override
    public void onLocationChanged(Location location) {
        Log.e("location", "location");

    }*/
