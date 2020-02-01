package net.rusnet.moviessearch.search.presentation;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.commons.domain.usecase.UseCase;
import net.rusnet.moviessearch.search.domain.model.Movie;
import net.rusnet.moviessearch.search.domain.model.SearchResult;
import net.rusnet.moviessearch.search.domain.usecase.PerformSearch;

import java.lang.ref.WeakReference;
import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    private WeakReference<SearchContract.View> mSearchViewWeakReference;
    private PerformSearch mPerformSearch;

    public SearchPresenter(@NonNull SearchContract.View view,
                           @NonNull PerformSearch performSearch) {
        mSearchViewWeakReference = new WeakReference<>(view);
        mPerformSearch = performSearch;
    }

    @Override
    public void performSearch(@NonNull String searchQuery) {
        mPerformSearch.execute(searchQuery,
                new UseCase.Callback<SearchResult>() {
                    @Override
                    public void onResult(@NonNull SearchResult result) {
                        switch (result.getSearchResultStatus()) {
                            case SUCCESSFUL:
                                showMovies(result.getMovieList());
                                break;
                            case ERROR:
                                showErrorMessage();
                                break;
                        }
                    }
                }
        );
    }

    private void showMovies(List<Movie> movieList) {
        SearchContract.View view = mSearchViewWeakReference.get();
        if (view != null) {
            view.showMovies(movieList);
        }
    }

    private void showErrorMessage() {
        SearchContract.View view = mSearchViewWeakReference.get();
        if (view != null) {
            view.showErrorMessage();
        }
    }
}

