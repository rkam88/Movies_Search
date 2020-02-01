package net.rusnet.moviessearch.search.domain.model;

import androidx.annotation.NonNull;

public class Movie {

    private final String mTitle;
    private final String mYear;
    private final String mPosterURL;

    public Movie(@NonNull String title, @NonNull String year, @NonNull String posterURL) {
        this.mTitle = title;
        this.mYear = year;
        this.mPosterURL = posterURL;
    }

    @NonNull
    public String getTitle() {
        return mTitle;
    }

    @NonNull
    public String getYear() {
        return mYear;
    }

    @NonNull
    public String getPosterURL() {
        return mPosterURL;
    }
}
