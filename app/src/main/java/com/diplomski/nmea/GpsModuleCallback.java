package com.diplomski.nmea;


import android.location.GnssStatus;
import android.location.Location;

/**
 * Callback invoked when new data is emitted from a
 * GPS module.
 */
public abstract class GpsModuleCallback {
    /**
     * Callback reporting GPS satellite status.
     *
     * @param status Latest status information from GPS module.
     */
    public abstract void onGpsSatelliteStatus(GnssStatus status);

    /**
     * Callback reporting an updated date/time from
     * the GPS satellite.
     *
     * @param timestamp Last received timestamp from GPS.
     */
    public abstract void onGpsTimeUpdate(long timestamp);

    /**
     * Callback reporting a location fix from the GPS module.
     *
     * @param location Latest location update.
     */
    public abstract void onGpsLocationUpdate(Location location);

    /**
     * Callback reporting raw NMEA sentences from the GPS module.
     *
     * @param nmeaMessage NMEA message data.
     */
    public abstract void onNmeaMessage(String nmeaMessage);
}
