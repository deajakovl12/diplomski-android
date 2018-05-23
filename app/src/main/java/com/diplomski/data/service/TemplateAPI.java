package com.diplomski.data.service;

import com.diplomski.data.api.models.request.FullRecordInfoRequest;
import com.diplomski.data.api.models.request.LoginRequest;
import com.diplomski.data.api.models.response.LoginApiResponse;
import com.diplomski.data.api.models.response.MovieApiResponse;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleSource;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import static com.diplomski.data.api.APIConstants.PATH_LOGIN;
import static com.diplomski.data.api.APIConstants.PATH_MOVIES;
import static com.diplomski.data.api.APIConstants.PATH_UPLOAD_RECORDS;


public interface TemplateAPI {


    @GET(PATH_MOVIES)
    Single<List<MovieApiResponse>> movieInfo();

    @POST(PATH_LOGIN)
    Single<LoginApiResponse> loginUser(@Body LoginRequest loginRequest);

    @POST(PATH_UPLOAD_RECORDS)
    Single<Response<Void>> uploadRecordsToServer(@Body List<FullRecordInfoRequest> fullRecordInfoRequests);

//    @Headers(CONTENT_TYPE_HEADER)
//    @POST(PATH_LOGIN)
//    Single<TypeSessionsApiResponse> login(@Body UserInformation userInformation);
//

//    @Headers({CONTENT_TYPE_HEADER, ACCEPT_HEADER})
//    @HTTP(method = "DELETE", path = PATH_CLAIM_DEVICE, hasBody = true)
//    Observable<Response<Void>> returnDevice(@Header("Authorization") String authorization, @Body User user,
//            @Path("person_id") long userId);

}
