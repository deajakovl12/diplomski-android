package com.diplomski.data.service;

import com.diplomski.data.api.models.response.MovieApiResponse;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

import static com.diplomski.data.api.APIConstants.PATH_MOVIES;


public interface TemplateAPI {


    @GET(PATH_MOVIES)
    Single<List<MovieApiResponse>> movieInfo();

//    @Headers(CONTENT_TYPE_HEADER)
//    @POST(PATH_LOGIN)
//    Single<TypeSessionsApiResponse> login(@Body UserInformation userInformation);
//

//    @Headers({CONTENT_TYPE_HEADER, ACCEPT_HEADER})
//    @HTTP(method = "DELETE", path = PATH_CLAIM_DEVICE, hasBody = true)
//    Observable<Response<Void>> returnDevice(@Header("Authorization") String authorization, @Body User user,
//            @Path("person_id") long userId);

}
