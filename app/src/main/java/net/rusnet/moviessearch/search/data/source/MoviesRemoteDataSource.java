package net.rusnet.moviessearch.search.data.source;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.search.data.model.OMDbMovie;
import net.rusnet.moviessearch.search.data.model.OMDbSearchResponse;
import net.rusnet.moviessearch.search.domain.model.Movie;
import net.rusnet.moviessearch.search.domain.model.SearchResult;
import net.rusnet.moviessearch.search.domain.model.SearchResultStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MoviesRemoteDataSource implements IMoviesRemoteDataSource {

    private static final String API_KEY = "3b6ee26";
    private static final String NO_POSTER = "N/A";
    private static final String EMPTY_STRING = "";
    private OmdbApi mOmdbApi;

    public MoviesRemoteDataSource(@NonNull OmdbApi omdbApi) {
        mOmdbApi = omdbApi;
    }

    @Override
    public void performSearch(@NonNull String query, @NonNull final onSearchResultCallback callback) {
        Call<OMDbSearchResponse> call = mOmdbApi.getResults(query, API_KEY);
        call.enqueue(new Callback<OMDbSearchResponse>() {

            @Override
            public void onResponse(Call<OMDbSearchResponse> call,
                                   Response<OMDbSearchResponse> response) {
                SearchResult searchResult;

                if (response.isSuccessful() &&
                        response.body() != null &&
                        response.body().getOMDbMovies() != null) {
                    List<OMDbMovie> omDbMovies = response.body().getOMDbMovies();
                    List<Movie> movieList = new ArrayList<>();

                    for (OMDbMovie omDbMovie : omDbMovies) {
                        Movie movie = new Movie(
                                omDbMovie.getTitle(),
                                omDbMovie.getYear(),
                                (omDbMovie.getPoster().equals(NO_POSTER) ? EMPTY_STRING : omDbMovie.getPoster())
                        );
                        movieList.add(movie);
                    }

                    String totalResultsAsString = response.body().getTotalResults();

                    searchResult = new SearchResult(
                            SearchResultStatus.SUCCESSFUL,
                            Long.parseLong(totalResultsAsString),
                            movieList);
                } else {
                    searchResult = new SearchResult(
                            SearchResultStatus.ERROR_REQUEST,
                            0,
                            null);
                }
                callback.onResult(searchResult);
            }

            @Override
            public void onFailure(Call<OMDbSearchResponse> call,
                                  Throwable t) {
                if (t instanceof IOException) {
                    callback.onResult(new SearchResult(
                            SearchResultStatus.ERROR_NETWORK,
                            0,
                            null));
                } else {
                    callback.onResult(new SearchResult(
                            SearchResultStatus.ERROR_OTHER,
                            0,
                            null));
                }
            }
        });
    }
}
