package net.rusnet.moviessearch.favorites.presentation;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.search.domain.model.Movie;

public interface FavoritesContract {
    interface View {

    }

    interface Presenter {
        void deleteFromFavorites(@NonNull Movie movie);
    }
}
