package net.rusnet.moviessearch.search.presentation;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.search.domain.model.Movie;

import java.util.List;

public interface SearchContract {

    interface View {
        void showMovies(@NonNull List<Movie> movieList,
                        @NonNull String searchQuery,
                        long totalResults);

        void updateMovies(@NonNull List<Movie> movieList);

        void showRequestErrorMessage();

        void showNetworkErrorMessage();

        void showOtherErrorMessage();
    }

    interface Presenter {
        void setView(@NonNull SearchContract.View view);

        void performSearch(@NonNull String searchQuery);

        void loadResultsPage(int pageToLoad, @NonNull String searchQuery);
    }

}
