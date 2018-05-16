package com.diplomski.domain.model;


public class RecordInfo {

    public static final RecordInfo EMPTY =
            new RecordInfo(0,0.0,0.0,0.0,0.0,"",0.0);

    public int idFullrecord;
    public double lat;
    public double lng;
    public double speed;
    public double speedLimit;
    public String currentDate;
    public double distanceFromLast;


    public RecordInfo() {
    }


    public RecordInfo(int idFullrecord, double lat, double lng, double speed, double speedLimit, String currentDate, double distanceFromLast) {
        this.idFullrecord = idFullrecord;
        this.lat = lat;
        this.lng = lng;
        this.speed = speed;
        this.speedLimit = speedLimit;
        this.currentDate = currentDate;
        this.distanceFromLast = distanceFromLast;
    }
}
