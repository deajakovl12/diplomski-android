package com.diplomski.data.api.models.response;


import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public final class MovieApiResponse {

    @SerializedName("title")
    String title;

    @SerializedName("image")
    String image;

    @SerializedName("rating")
    float rating;

    @SerializedName("releaseYear")
    int releaseYear;

    @SerializedName("genre")
    ArrayList<String> genre;

    public MovieApiResponse(String title, String image, float rating, int releaseYear, ArrayList<String> genre) {
        this.title = title;
        this.image = image;
        this.rating = rating;
        this.releaseYear = releaseYear;
        this.genre = genre;
    }

    public String getTitle() {
        return title;
    }

    public String getImage() {
        return image;
    }

    public float getRating() {
        return rating;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public ArrayList<String> getGenre() {
        return genre;
    }
}



