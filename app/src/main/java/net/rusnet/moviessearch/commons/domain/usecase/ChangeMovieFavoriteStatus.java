package net.rusnet.moviessearch.commons.domain.usecase;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import net.rusnet.moviessearch.commons.data.source.IMoviesLocalDataSource;
import net.rusnet.moviessearch.search.domain.model.Movie;

public class ChangeMovieFavoriteStatus extends DBUseCase<Movie, Void> {

    public ChangeMovieFavoriteStatus(@NonNull AsyncUseCaseExecutor useCaseExecutor, @NonNull IMoviesLocalDataSource moviesLocalDataSource) {
        super(useCaseExecutor, moviesLocalDataSource);
    }

    @NonNull
    @Override
    protected Void doInBackground(@Nullable Movie requestValues) {
        if (requestValues == null) return null;
        if (requestValues.isInFavorites()) {
            mMoviesLocalDataSource.addMovie(requestValues);
        } else {
            mMoviesLocalDataSource.deleteMovie(requestValues.getImdbID());
        }
        return null;
    }
}
