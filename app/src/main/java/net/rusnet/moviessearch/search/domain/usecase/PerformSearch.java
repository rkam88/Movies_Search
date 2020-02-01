package net.rusnet.moviessearch.search.domain.usecase;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.commons.domain.usecase.UseCase;
import net.rusnet.moviessearch.search.data.source.IMoviesRemoteDataSource;
import net.rusnet.moviessearch.search.domain.model.SearchResult;

public class PerformSearch extends UseCase<String, SearchResult> {

    private IMoviesRemoteDataSource mMoviesRemoteDataSource;

    public PerformSearch(@NonNull IMoviesRemoteDataSource moviesRemoteDataSource) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
    }

    @Override
    public void execute(@NonNull String requestValues, @NonNull final Callback<SearchResult> callback) {
        mMoviesRemoteDataSource.performSearch(requestValues, new IMoviesRemoteDataSource.onSearchResultCallback() {
            @Override
            public void onResult(@NonNull SearchResult searchResult) {
                callback.onResult(searchResult);
            }
        });
    }

}
