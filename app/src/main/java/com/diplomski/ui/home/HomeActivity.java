package com.diplomski.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.diplomski.R;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.data.api.models.response.MovieApiResponse;
import com.diplomski.device.ForegroundService;
import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;
import com.diplomski.injection.component.ActivityComponent;
import com.diplomski.ui.base.activities.BaseActivity;
import com.diplomski.util.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.simplify.ink.InkView;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import timber.log.Timber;


public class HomeActivity extends BaseActivity implements HomeView {


    private static final float MINSTROKEWIDTH = 0.75f;
    private static final float MAXSTROKEWIDTH = 3f;
    private static final int COMPRESS_QUALITY = 100;

    private static final String LOGIN_EXTRA = "login_extra";

    @Inject
    HomePresenter presenter;

    @BindView(R.id.text_millis)
    TextView textView;

    @BindView(R.id.text_location)
    TextView textViewLocation;

    @BindView(R.id.text_distance)
    TextView textViewDistance;

    @BindView(R.id.text_speed)
    TextView textViewSpeed;

    @BindView(R.id.button_zapocni)
    Button buttonStartStop;

    @BindView(R.id.signature_capture_canvas)
    InkView signatureCanvas;

    @BindView(R.id.signature_capture_clear_canvas)
    TextView textViewClearCanvas;

    @BindView(R.id.login_user_name)
    TextView txtUserFirsNameLastName;


    BroadcastReceiver broadcastReceiverTimer = null;
    BroadcastReceiver broadcastReceiverLocation = null;

    boolean started = false;
    private boolean isSignatureAdded = false;

    public static Intent createIntent(final Context context, final LoginApiResponse loginApiResponse) {
        return new Intent(context, HomeActivity.class).putExtra(LOGIN_EXTRA, loginApiResponse);
    }

    LoginApiResponse loginApiResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ButterKnife.bind(this);

        loginApiResponse = getIntent().getParcelableExtra(LOGIN_EXTRA);

        txtUserFirsNameLastName.setText(loginApiResponse.id + " " + loginApiResponse.ime + " " +loginApiResponse.prezime + " " + loginApiResponse.adresa + " " +loginApiResponse.username + " " +loginApiResponse.password + " " +loginApiResponse.isAdmin);
//        checkLocationPermission();

        signatureCanvas.setColor(getResources().getColor(android.R.color.black));
        signatureCanvas.setMinStrokeWidth(MINSTROKEWIDTH);
        signatureCanvas.setMaxStrokeWidth(MAXSTROKEWIDTH);
        setOnEditorActionListeners();

    }

    private void setOnEditorActionListeners() {
        signatureCanvas.addListener(new InkView.InkListener() {
            @Override
            public void onInkClear() {
                isSignatureAdded = false;
            }

            @Override
            public void onInkDraw() {
                isSignatureAdded = true;
            }
        });
    }

    private void checkLocationPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            isLocationEnabled();
                        }

                        if (!report.getDeniedPermissionResponses().isEmpty()) {
                            finish();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();

    }

    public void isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("GPS not enabled");
            builder.setMessage("GPS is not enabled. Enable GPS?");
            builder.setPositiveButton("YES", (dialogInterface, i) -> this.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)));
            builder.setNegativeButton("NO", (dialogInterface, i) -> this.finish());
            builder.create().show();
            return;
        }
    }

    @OnClick(R.id.signature_capture_clear_canvas)
    public void clearCanvas() {
        signatureCanvas.clear();
        isSignatureAdded = false;
    }


    @OnClick(R.id.button_zapocni)
    public void startFollow() {

        if (!started) {
            if (isSignatureAdded) {
                Bitmap canvasImage = signatureCanvas.getBitmap(getResources().getColor(android.R.color.white));
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                canvasImage.compress(Bitmap.CompressFormat.JPEG, COMPRESS_QUALITY, bs);
                byte[] bytes = bs.toByteArray();
                String encodedImage = Base64.encodeToString(bytes, Base64.DEFAULT);
                Log.e("Signature", encodedImage);


                FullRecordingInfo fullRecordingInfo = new FullRecordingInfo();

                fullRecordingInfo.dateStart = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss:SS", Locale.getDefault()).format(new Date());
                fullRecordingInfo.distanceTraveled = 0;
                //TODO add image
                fullRecordingInfo.image = "";

                fullRecordingInfo.userId = String.valueOf(loginApiResponse.id);
                fullRecordingInfo.signature = encodedImage;
                fullRecordingInfo.sentToServer = 1;

                presenter.saveFullRecordToDb(fullRecordingInfo);
                started = true;
                buttonStartStop.setText("Zavrsi");
                startService(new Intent(HomeActivity.this, ForegroundService.class)
                        .setAction(Constants.ACTION.STARTFOREGROUND_ACTION));

                registerBroadCastReceiverTimer();
                registerBroadCastReceiverLocation();
            } else {
                Toast.makeText(this, "ADD SIGNATURE!", Toast.LENGTH_SHORT).show();
            }

        } else {
            started = false;
            buttonStartStop.setText("Zapocni");
            if (broadcastReceiverTimer != null) {
                unregisterReceiver(broadcastReceiverTimer);
            }
            if (broadcastReceiverLocation != null) {
                unregisterReceiver(broadcastReceiverLocation);
            }
            startService(new Intent(HomeActivity.this, ForegroundService.class).setAction(Constants.ACTION.STOPFOREGROUND_ACTION));

        }
    }

    private void registerBroadCastReceiverTimer() {
        broadcastReceiverTimer = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                long timeInMilliseconds = intent.getLongExtra("timeInMilliseconds", 0);
                textView.setText(String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(timeInMilliseconds),
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
        registerReceiver(broadcastReceiverTimer, new IntentFilter(Constants.ACTION.BROADCAST_ACTION));
    }

    private void registerBroadCastReceiverLocation() {
        broadcastReceiverLocation = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Location location = intent.getParcelableExtra("newLocation");
                double distance = intent.getDoubleExtra("newDistance", 0);
                double distanceLastTwo = intent.getDoubleExtra("newDistanceLastTwo", 0);
                double speed = intent.getDoubleExtra("newSpeed", 0);
                if (location != null) {
                    textViewLocation.setText(location.getLatitude() + " -- " + location.getLongitude());
                }
                textViewDistance.setText(distance + " m");
                textViewSpeed.setText(speed + " km/h");


                //TODO HERE ALSO CALL UPDATE ON FULL RECORD (DISTANCE)
                if (location != null) {
                    RecordInfo recordInfo = new RecordInfo();
                    recordInfo.currentDate = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss:SS", Locale.getDefault()).format(new Date());
                    recordInfo.distanceFromLast = distanceLastTwo;
                    recordInfo.lat = location.getLatitude();
                    recordInfo.lng = location.getLongitude();
                    recordInfo.speed = speed;
                    //TODO CALL TO GET SPEED LIMIT FROM A ROAD
                    recordInfo.speedLimit = 0;
                    presenter.saveRecordToDb(recordInfo, distance);
                }

            }
        };
        registerReceiver(broadcastReceiverLocation, new IntentFilter(Constants.ACTION.BROADCAST_ACTION_LOCATION));
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.setView(this);
        //presenter.getMovieInfo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (broadcastReceiverTimer != null) {
            unregisterReceiver(broadcastReceiverTimer);
        }

        if (broadcastReceiverLocation != null) {
            unregisterReceiver(broadcastReceiverLocation);
        }
        startService(new Intent(HomeActivity.this, ForegroundService.class).setAction(Constants.ACTION.STOPFOREGROUND_ACTION));

    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.dispose();
    }

    @Override
    protected void inject(final ActivityComponent activityComponent) {
        activityComponent.inject(this);
    }

    @Override
    public void showData(final MovieApiResponse movieInfo) {
        if (movieInfo != null) {
            Timber.e(movieInfo.getAccuracy() + " " + movieInfo.getLocation().getLat() + " " + movieInfo.getLocation().getLng());
        } else {
            Timber.e("NULL");
        }
    }

    @Override
    public void recordingStarted() {
        Toast.makeText(this, "Recording Started!", Toast.LENGTH_SHORT).show();
    }
}
