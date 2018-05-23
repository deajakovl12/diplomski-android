package com.diplomski.data.api.models.request;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class FullRecordInfoRequest {

    @SerializedName("idSnimanja")
    public String fullRecordIdDB;
    @SerializedName("idKorisnika")
    public String userId;
    @SerializedName("datum")
    public String dateStart;
    @SerializedName("predjenaUdaljenost")
    public double distanceTraveled;
    @SerializedName("slikaKorisnika")
    public String image;
    @SerializedName("potpisKorisnika")
    public String signature;
    @SerializedName("listOfRecords")
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

        @SerializedName("idTreninga")
        public String idFullrecord;
        @SerializedName("lat")
        public double lat;
        @SerializedName("lng")
        public double lng;
        @SerializedName("speed")
        public double speed;
        @SerializedName("speedLimit")
        public double speedLimit;
        @SerializedName("datum")
        public String currentDate;
        @SerializedName("udaljenostZadnjeDvije")
        public double distanceFromLast;

        public OneRecordInfoRequest(String idFullrecord, double lat, double lng, double speed, double speedLimit, String currentDate, double distanceFromLast) {
            this.idFullrecord = idFullrecord;
            this.lat = lat;
            this.lng = lng;
            this.speed = speed;
            this.speedLimit = speedLimit;
            this.currentDate = currentDate;
            this.distanceFromLast = distanceFromLast;
        }
    }
}
