package net.rusnet.moviessearch.search.presentation;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.commons.domain.usecase.UseCase;
import net.rusnet.moviessearch.search.domain.model.Movie;
import net.rusnet.moviessearch.search.domain.model.SearchResult;
import net.rusnet.moviessearch.search.domain.model.SearchResultStatus;
import net.rusnet.moviessearch.search.domain.usecase.LoadResultsPage;
import net.rusnet.moviessearch.search.domain.usecase.LoadResultsPageRequestValues;
import net.rusnet.moviessearch.search.domain.usecase.PerformSearch;

import java.lang.ref.WeakReference;
import java.util.List;

public class SearchPresenter implements SearchContract.Presenter {

    private WeakReference<SearchContract.View> mSearchViewWeakReference;
    private PerformSearch mPerformSearch;
    private LoadResultsPage mLoadResultsPage;

    public SearchPresenter(@NonNull PerformSearch performSearch,
                           @NonNull LoadResultsPage loadResultsPage) {
        mPerformSearch = performSearch;
        mLoadResultsPage = loadResultsPage;
    }

    @Override
    public void setView(@NonNull SearchContract.View view) {
        mSearchViewWeakReference = new WeakReference<>(view);
    }

    @Override
    public void performSearch(@NonNull final String searchQuery) {
        mPerformSearch.execute(searchQuery,
                new UseCase.Callback<SearchResult>() {
                    @Override
                    public void onResult(@NonNull SearchResult result) {
                        switch (result.getSearchResultStatus()) {
                            case SUCCESSFUL:
                                showMovies(result.getMovieList(),
                                        searchQuery,
                                        result.getTotalResults());
                                break;
                            case ERROR_REQUEST:
                            case ERROR_NETWORK:
                            case ERROR_OTHER:
                                showErrorMessage(result.getSearchResultStatus());
                                break;
                        }
                    }
                }
        );
    }

    @Override
    public void loadResultsPage(int pageToLoad, @NonNull String searchQuery) {
        mLoadResultsPage.execute(new LoadResultsPageRequestValues(pageToLoad, searchQuery),
                new UseCase.Callback<SearchResult>() {
                    @Override
                    public void onResult(@NonNull SearchResult result) {
                        switch (result.getSearchResultStatus()) {
                            case SUCCESSFUL:
                                updateMovies(result.getMovieList());
                                break;
                            case ERROR_REQUEST:
                            case ERROR_NETWORK:
                            case ERROR_OTHER:
                                showErrorMessage(result.getSearchResultStatus());
                                break;
                        }
                    }
                });
    }

    private void showMovies(@NonNull List<Movie> movieList,
                            @NonNull String searchQuery,
                            long totalResults) {
        SearchContract.View view = mSearchViewWeakReference.get();
        if (view != null) {
            view.showMovies(movieList, searchQuery, totalResults);
        }
    }

    private void updateMovies(List<Movie> movieList) {
        SearchContract.View view = mSearchViewWeakReference.get();
        if (view != null) {
            view.updateMovies(movieList);
        }
    }

    private void showErrorMessage(@NonNull SearchResultStatus searchResultStatus) {
        SearchContract.View view = mSearchViewWeakReference.get();
        if (view != null) {
            switch (searchResultStatus) {
                case ERROR_REQUEST:
                    view.showRequestErrorMessage();
                    break;
                case ERROR_NETWORK:
                    view.showNetworkErrorMessage();
                    break;
                case ERROR_OTHER:
                    view.showOtherErrorMessage();
                    break;
            }
        }
    }
}

