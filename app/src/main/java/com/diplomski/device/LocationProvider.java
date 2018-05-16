package com.diplomski.device;


import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;


public class LocationProvider {

    /*private LocationRequest mLocationRequest;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private long UPDATE_INTERVAL = 5000;
    private long FASTEST_INTERVAL = 4000;

    private Context context;

    private Location currentLocation;

    public LocationProvider(Context context) {
        this.context = context;
        fusedLocationProviderClient = new FusedLocationProviderClient(context);

    }

    public void stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        }

    }

    public Location getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(Location currentLocation) {
        this.currentLocation = currentLocation;
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        LocationSettingsRequest locationSettingsRequest = builder.build();

        SettingsClient settingsClient = LocationServices.getSettingsClient(context);
        settingsClient.checkLocationSettings(locationSettingsRequest);


        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest,
                locationCallback = new LocationCallback() {
                    @Override
                    public void onLocationResult(LocationResult locationResult) {
                        onLocationChanged(locationResult.getLastLocation());
                    }
                },
                Looper.myLooper());
    }


    public void onLocationChanged(Location location) {
        // New location has now been determined
        String msg = "Updated Location: " +
                Double.toString(location.getLatitude()) + "," +
                Double.toString(location.getLongitude());
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        // You can now create a LatLng Object for use with maps
        //LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        Log.e("LOCATIOn", location.getLatitude() + " " + location.getLongitude());
        setCurrentLocation(location);
    }

    public double calculateDistance(Location oldLocation, Location currentLocation) {
        return oldLocation.distanceTo(currentLocation);
    }
*/
}
