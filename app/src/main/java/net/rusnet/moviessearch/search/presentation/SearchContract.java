package net.rusnet.moviessearch.search.presentation;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.search.domain.model.Movie;

import java.util.List;

public interface SearchContract {

    interface View {
        void showMovies(List<Movie> movieList);

        void showErrorMessage();
    }

    interface Presenter {
        void performSearch(@NonNull String searchQuery);
    }

}
