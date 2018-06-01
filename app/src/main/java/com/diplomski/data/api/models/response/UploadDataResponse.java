package com.diplomski.data.api.models.response;


import retrofit2.Response;

public class UploadDataResponse {

    public Response<Void> response;
    public double distanceTraveled;

    public UploadDataResponse(Response<Void> response, double distanceTraveled) {
        this.response = response;
        this.distanceTraveled = distanceTraveled;
    }
}
