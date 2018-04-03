package com.diplomski.domain.usecase;


import com.diplomski.data.api.models.response.MovieApiResponse;
import com.diplomski.data.service.NetworkService;
import com.diplomski.data.storage.TemplatePreferences;

import java.util.List;

import io.reactivex.Single;

public class MovieUseCaseImpl implements MovieUseCase {

    private final NetworkService networkService;

    private final TemplatePreferences preferences;


    public MovieUseCaseImpl(NetworkService networkService, TemplatePreferences preferences) {
        this.networkService = networkService;
        this.preferences = preferences;
    }

    @Override
    public Single<List<MovieApiResponse>> getMovieInfo() {
        return Single
                .defer(() -> networkService.movieInfo());
    }

}
