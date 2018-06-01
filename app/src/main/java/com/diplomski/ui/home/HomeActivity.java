package com.diplomski.ui.home;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.media.ImageReader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.diplomski.R;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.data.api.models.response.MovieApiResponse;
import com.diplomski.device.Camera;
import com.diplomski.device.ForegroundService;
import com.diplomski.domain.model.FullRecordingInfo;
import com.diplomski.domain.model.RecordInfo;
import com.diplomski.injection.component.ActivityComponent;
import com.diplomski.ui.base.activities.BaseActivity;
import com.diplomski.ui.login.LoginActivity;
import com.diplomski.ui.wifiactivity.WifiActivity;
import com.diplomski.util.Constants;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.simplify.ink.InkView;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
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

import static com.diplomski.injection.module.DataModule.PREFS_NAME;


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

    @BindView(R.id.image_taken)
    ImageView takenImage;

    @BindView(R.id.fab)
    FloatingActionButton fabMain;


    @BindView(R.id.fab1)
    FloatingActionButton fabChild1;


    @BindView(R.id.fab2)
    FloatingActionButton fabChild2;


    @BindView(R.id.fab3)
    FloatingActionButton fabChild3;

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.connected_wifi)
    TextView connectedWifi;


    BroadcastReceiver broadcastReceiverTimer = null;
    BroadcastReceiver broadcastReceiverLocation = null;

    boolean started = false;
    private boolean isSignatureAdded = false;
    private boolean isImageTaken = false;

    private Camera mCamera;
    private Handler mCameraHandler;
    private HandlerThread mCameraThread;

    private String saveTakenImageBase64;
    private boolean isFABOpen;


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
//        checkLocationPermission();
        showUserData();

        signatureCanvas.setColor(ContextCompat.getColor(this, android.R.color.black));
        signatureCanvas.setMinStrokeWidth(MINSTROKEWIDTH);
        signatureCanvas.setMaxStrokeWidth(MAXSTROKEWIDTH);
        signatureCanvas.setOnTouchListener((v, event) -> {
            v.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });
        setOnEditorActionListeners();

        mCameraThread = new HandlerThread("CameraBackground");
        mCameraThread.start();
        mCameraHandler = new Handler(mCameraThread.getLooper());

        // Camera code is complicated, so we've shoved it all in this closet class for you.
        mCamera = Camera.getInstance();
        mCamera.initializeCamera(this, mCameraHandler, mOnImageAvailableListener);



    }

    /**
     * Listener for new camera images.
     */
    private ImageReader.OnImageAvailableListener mOnImageAvailableListener =
            reader -> {
                Image image = reader.acquireLatestImage();
                // get image bytes
                ByteBuffer imageBuf = image.getPlanes()[0].getBuffer();
                final byte[] imageBytes = new byte[imageBuf.remaining()];
                imageBuf.get(imageBytes);
                image.close();
                HomeActivity.this.runOnUiThread(() -> onPictureTaken(imageBytes));
            };

    /**
     * Upload image data to Firebase as a doorbell event.
     */

    private void onPictureTaken(final byte[] imageBytes) {
        if (imageBytes != null) {
//
            Log.e("IAGEMGMEEGM", imageBytes.toString() + " a");

            Bitmap bmp = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            takenImage.setImageBitmap(bmp);
            isImageTaken = true;
            saveTakenImageBase64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            Log.e("Signature base64", saveTakenImageBase64);
        }
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

    @Override
    public void resetAllToStart(boolean needRefreshUserData) {
        signatureCanvas.clear();
        takenImage.setImageDrawable(null);
        textView.setText("");
        textViewLocation.setText("");
        textViewDistance.setText("");
        textViewSpeed.setText("");
        if(needRefreshUserData) {
            presenter.updateTraveledDistance();
        }
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

    private void showFABMenu() {
        isFABOpen = true;
        fabChild1.animate().translationY(-60);
        fabChild2.animate().translationY(-110);
        fabChild3.animate().translationY(-160);
    }

    private void closeFABMenu() {
        isFABOpen = false;
        fabChild1.animate().translationY(0);
        fabChild2.animate().translationY(0);
        fabChild3.animate().translationY(0);
    }

    @OnClick(R.id.fab)
    public void mainFabClick() {
        if (!isFABOpen) {
            showFABMenu();
        } else {
            closeFABMenu();
        }
    }

    @OnClick(R.id.fab1)
    public void fabChild1Click() {
        presenter.checkDataForUploadLogout();
    }

    @OnClick(R.id.fab2)
    public void fabChild2Click() {
        startActivity(new Intent(HomeActivity.this, WifiActivity.class));

    }

    @OnClick(R.id.fab3)
    public void fabChild3Click() {
        Toast.makeText(this, "3", Toast.LENGTH_SHORT).show();
    }


    @OnClick(R.id.signature_capture_clear_canvas)
    public void clearCanvas() {
        signatureCanvas.clear();
        isSignatureAdded = false;
    }

    @OnClick(R.id.button_slika)
    public void takePicture() {
        mCamera.takePicture();
    }


    @OnClick(R.id.button_zapocni)
    public void startFollow() {

        if (!started) {
            if (isSignatureAdded && isImageTaken) {
                isSignatureAdded = false;
                isImageTaken = false;
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
                fullRecordingInfo.image = saveTakenImageBase64;

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
                Toast.makeText(this, "POTPISITE SE I POSLIKAJTE KAO EVIDENCIJA!", Toast.LENGTH_SHORT).show();
            }

        } else {
            started = false;
            buttonStartStop.setText("Zapocni");
            try {
                if (broadcastReceiverTimer != null) {
                    unregisterReceiver(broadcastReceiverTimer);
                }
                if (broadcastReceiverLocation != null) {
                    unregisterReceiver(broadcastReceiverLocation);
                }
            } catch (Exception e) {
                Timber.e("VEC ZAUSTAVLJENI RECEIVERI");
            }
            startService(new Intent(HomeActivity.this, ForegroundService.class).setAction(Constants.ACTION.STOPFOREGROUND_ACTION));

            createSnimanjeZavrseno();
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


                if (location != null) {
                    RecordInfo recordInfo = new RecordInfo();
                    recordInfo.currentDate = new SimpleDateFormat("dd.MM.yyyy - HH:mm:ss:SS", Locale.getDefault()).format(new Date());
                    recordInfo.distanceFromLast = distanceLastTwo;
                    recordInfo.lat = location.getLatitude();
                    recordInfo.lng = location.getLongitude();
                    recordInfo.speed = speed;
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
        presenter.checkDataForUpload();

        WifiManager wifiManager = (WifiManager) getSystemService (Context.WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo ();
        connectedWifi.setText(info.getSSID());
        //presenter.getMovieInfo();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null;
    }

    private void createAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ne poslana snimanja");
        builder.setCancelable(false);
        builder.setMessage("Postoje snimanja koja nisu poslana, potrebno je poslati snimanja kako bi imali uvid u njih.");
        builder.setPositiveButton(
                "Pošalji",
                (dialog, id) -> {
                    if (isNetworkAvailable()) {
                        presenter.uploadRecordsToServer();
                    } else {
                        Toast.makeText(HomeActivity.this, "Nema internet konekcije, ne mogu poslati podatke!", Toast.LENGTH_SHORT).show();
                    }
                });
        builder.setNegativeButton(
                "Ne sada",
                (dialog, id) -> dialog.dismiss());

        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    private void createLogoutDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Odjavi se");
        builder.setCancelable(false);
        builder.setMessage("Postoje snimanja koja nisu poslana, ako se odjavite, ta snimanja će biti izgubljena! Odjavi se?");
        builder.setPositiveButton(
                "DA",
                (dialog, id) -> {
                    getApplicationContext().getSharedPreferences(PREFS_NAME, 0).edit().clear().apply();
                    presenter.removeDataFromDb();
                });
        builder.setNegativeButton(
                "NE",
                (dialog, id) -> dialog.dismiss());

        AlertDialog alert11 = builder.create();
        alert11.show();
    }


    private void createSnimanjeZavrseno() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Snimanje završeno");
        builder.setCancelable(false);
        builder.setMessage("Da li želite poslati podatke snimanja? Ne poslani podaci ne ulaze u smanjenje vaše kazne!");
        builder.setPositiveButton(
                "Pošalji",
                (dialog, id) -> {
                    if (isNetworkAvailable()) {
                        presenter.uploadRecordsToServer();
                    } else {
                        Toast.makeText(HomeActivity.this, "Nema internet konekcije, ne mogu poslati podatke!", Toast.LENGTH_SHORT).show();
                        resetAllToStart(false);
                    }
                });
        builder.setNegativeButton(
                "Ne sada",
                (dialog, id) -> {
                    dialog.dismiss();
                    resetAllToStart(false);
                });

        AlertDialog alert11 = builder.create();
        alert11.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCamera.shutDown();

        mCameraThread.quitSafely();

        try {
            if (broadcastReceiverTimer != null) {
                unregisterReceiver(broadcastReceiverTimer);
            }


            if (broadcastReceiverLocation != null) {
                unregisterReceiver(broadcastReceiverLocation);
            }
        } catch (Exception e) {
            Timber.e("vec zaustavljen receiver!");
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

    @Override
    public void needDataUpload(Boolean notSent) {
        if (notSent) {
            createAlertDialog();
        } else {
            Timber.e("OK");
        }
    }

    @Override
    public void showErroUploadMessage() {
        Toast.makeText(this, "Podaci se nisu poslali, pokušajte kasnije!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void hasData(Boolean notSent) {
        if (notSent) {
            createLogoutDialog();
        } else {
            getApplicationContext().getSharedPreferences(PREFS_NAME, 0).edit().clear().apply();
            presenter.removeDataFromDb();
        }
    }

    @Override
    public void logoutUser() {
        startActivity(new Intent(HomeActivity.this, LoginActivity.class));
    }

    @Override
    public void updateUserDistance(LoginApiResponse loginApiResponse) {
        this.loginApiResponse = loginApiResponse;
        showUserData();
    }

    private void showUserData(){
        txtUserFirsNameLastName.setText(String.format("%d %s %s %s %s %d %f %f", loginApiResponse.id, loginApiResponse.ime, loginApiResponse.prezime, loginApiResponse.adresa, loginApiResponse.username, loginApiResponse.isAdmin, loginApiResponse.pocetnaKazna, loginApiResponse.preostaloKazne));
    }
}
