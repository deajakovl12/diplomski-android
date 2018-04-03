package com.diplomski.data.api.converter;


import com.diplomski.data.api.models.response.MovieApiResponse;
import com.diplomski.domain.model.MovieInfo;

import java.util.List;


public interface MovieAPIConverter {

    List<MovieInfo> convertToMovieInfo(List<MovieApiResponse> movieApiResponse);

}
