package net.rusnet.moviessearch.search.data.source;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.search.domain.model.SearchResult;

public interface IMoviesRemoteDataSource {

    interface onSearchResultCallback {
        void onResult(@NonNull SearchResult searchResult);
    }

    void performSearch(@NonNull String query,
                       @NonNull onSearchResultCallback callback);

}
