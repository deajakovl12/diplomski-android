package com.diplomski.data.api.converter;


import com.diplomski.data.api.models.response.MovieApiResponse;
import com.diplomski.domain.model.MovieInfo;

import java.util.ArrayList;
import java.util.List;


public class MovieAPIConverterImpl implements MovieAPIConverter {

    @Override
    public List<MovieInfo> convertToMovieInfo(final List<MovieApiResponse> movieApiResponse) {

        List<MovieInfo> movieInfoList = new ArrayList<>(movieApiResponse.size());

        for (MovieApiResponse apiResponse : movieApiResponse) {
            if (apiResponse == null) {
                movieInfoList.add(MovieInfo.EMPTY);
            } else {
                movieInfoList.add(new MovieInfo(apiResponse.getTitle(), apiResponse.getImage()));
            }
        }
        return  movieInfoList;
    }
}
