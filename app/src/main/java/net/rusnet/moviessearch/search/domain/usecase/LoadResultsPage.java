package net.rusnet.moviessearch.search.domain.usecase;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.commons.domain.usecase.UseCase;
import net.rusnet.moviessearch.search.data.source.IMoviesRemoteDataSource;
import net.rusnet.moviessearch.search.domain.model.SearchResult;

public class LoadResultsPage extends UseCase<LoadResultsPageRequestValues, SearchResult> {

    private IMoviesRemoteDataSource mMoviesRemoteDataSource;

    public LoadResultsPage(@NonNull IMoviesRemoteDataSource moviesRemoteDataSource) {
        mMoviesRemoteDataSource = moviesRemoteDataSource;
    }

    @Override
    public void execute(@NonNull LoadResultsPageRequestValues requestValues, @NonNull final Callback<SearchResult> callback) {
        mMoviesRemoteDataSource.getPage(requestValues.getSearchQuery(),
                requestValues.getPageToLoad(),
                new IMoviesRemoteDataSource.onSearchResultCallback() {
                    @Override
                    public void onResult(@NonNull SearchResult searchResult) {
                        callback.onResult(searchResult);
                    }
                });
    }

}
