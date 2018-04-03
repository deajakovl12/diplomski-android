package com.diplomski.ui.home;

import com.diplomski.domain.model.MovieInfo;

import java.util.List;


public interface HomeView {

    void showData(List<MovieInfo> movieInfo);
}
