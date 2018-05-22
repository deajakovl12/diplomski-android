package com.diplomski.data.api.models.request;


import java.util.ArrayList;
import java.util.List;

public class FullRecordInfoRequest {

    public String fullRecordIdDB;
    public String userId;
    public String dateStart;
    public double distanceTraveled;
    public String image;
    public String signature;
    public List<OneRecordInfoRequest> oneRecordList;

    public FullRecordInfoRequest(String fullRecordIdDB, String userId, String dateStart, double distanceTraveled, String image, String signature) {
        this.fullRecordIdDB = fullRecordIdDB;
        this.userId = userId;
        this.dateStart = dateStart;
        this.distanceTraveled = distanceTraveled;
        this.image = image;
        this.signature = signature;
        this.oneRecordList = new ArrayList<>();
    }

    public void addOneRecordToList(OneRecordInfoRequest oneRecordInfoRequest){
        oneRecordList.add(oneRecordInfoRequest);
    }

    public static class OneRecordInfoRequest{
        public int oneRecordId;
        public String idFullrecord;
        public double lat;
        public double lng;
        public double speed;
        public double speedLimit;
        public String currentDate;
        public double distanceFromLast;

        public OneRecordInfoRequest(String idFullrecord, double lat, double lng, double speed, double speedLimit, String currentDate, double distanceFromLast, int oneRecordId) {
            this.idFullrecord = idFullrecord;
            this.lat = lat;
            this.lng = lng;
            this.speed = speed;
            this.speedLimit = speedLimit;
            this.currentDate = currentDate;
            this.distanceFromLast = distanceFromLast;
            this.oneRecordId = oneRecordId;
        }
    }
}
