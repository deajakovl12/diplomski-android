package com.diplomski.domain.usecase;


import com.diplomski.data.api.models.response.MovieApiResponse;

import java.util.List;

import io.reactivex.Single;

public interface MovieUseCase {

    Single<List<MovieApiResponse>> getMovieInfo();

}
