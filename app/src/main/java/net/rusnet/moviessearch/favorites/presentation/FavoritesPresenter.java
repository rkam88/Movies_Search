package net.rusnet.moviessearch.favorites.presentation;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.commons.domain.usecase.ChangeMovieFavoriteStatus;
import net.rusnet.moviessearch.commons.domain.usecase.UseCase;
import net.rusnet.moviessearch.search.domain.model.Movie;

public class FavoritesPresenter implements FavoritesContract.Presenter {

    private ChangeMovieFavoriteStatus mChangeMovieFavoriteStatus;

    public FavoritesPresenter(@NonNull ChangeMovieFavoriteStatus changeMovieFavoriteStatus) {
        mChangeMovieFavoriteStatus = changeMovieFavoriteStatus;
    }

    @Override
    public void deleteFromFavorites(@NonNull Movie movie) {
        movie.setInFavorites(false);
        mChangeMovieFavoriteStatus.execute(movie, new UseCase.Callback<Void>() {
            @Override
            public void onResult(@NonNull Void result) {

            }
        });
    }
}
