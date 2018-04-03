package com.diplomski.domain.model;


public class MovieInfo {

    public static final MovieInfo EMPTY = new MovieInfo("", "");

    final String title;

    final String imageUrl;

    public MovieInfo(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
