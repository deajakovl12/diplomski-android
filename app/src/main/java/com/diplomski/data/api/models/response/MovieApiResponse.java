package com.diplomski.data.api.models.response;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public final class MovieApiResponse {

    @SerializedName("location")
    Location location;

    @SerializedName("accuracy")
    double accuracy;

    public class Location{

        @SerializedName("lat")
        double lat;
        @SerializedName("lng")
        double lng;

        public Location(double lat, double lng) {
            this.lat = lat;
            this.lng = lng;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }

    public MovieApiResponse(Location location, double accuracy) {
        this.location = location;
        this.accuracy = accuracy;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public double getAccuracy() {
        return accuracy;
    }

    public void setAccuracy(double accuracy) {
        this.accuracy = accuracy;
    }
}



