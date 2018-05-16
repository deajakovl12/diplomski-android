package com.diplomski.domain.model;


public class FullRecordingInfo {

    public static final FullRecordingInfo EMPTY =
            new FullRecordingInfo(0,"","",0,0,"","");

    public int fullRecordIdDB;
    public String userId;
    public String dateStart;
    public int sentToServer;
    public double distanceTraveled;
    public String image;
    public String signature;


    public FullRecordingInfo() {
    }

    public FullRecordingInfo(int fullRecordIdDB, String userId, String dateStart, int sentToServer, double distanceTraveled, String image, String signature) {
        this.fullRecordIdDB = fullRecordIdDB;
        this.userId = userId;
        this.dateStart = dateStart;
        this.sentToServer = sentToServer;
        this.distanceTraveled = distanceTraveled;
        this.image = image;
        this.signature = signature;
    }
}
