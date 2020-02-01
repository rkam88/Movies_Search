package net.rusnet.moviessearch.search.data.source;

import androidx.annotation.NonNull;

import net.rusnet.moviessearch.search.data.model.OMDbMovie;
import net.rusnet.moviessearch.search.data.model.OMDbSearchResponse;
import net.rusnet.moviessearch.search.domain.model.Movie;
import net.rusnet.moviessearch.search.domain.model.SearchResult;
import net.rusnet.moviessearch.search.domain.model.SearchResultStatus;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MoviesRemoteDataSource implements IMoviesRemoteDataSource {

    private static final String BASE_URL = "http://omdbapi.com/";
    private static final String API_KEY = "3b6ee26";
    private Retrofit mRetrofit;

    public MoviesRemoteDataSource() {
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }


    @Override
    public void performSearch(@NonNull String query, @NonNull final onSearchResultCallback callback) {
        OMDbEndpointInterface omDbApi = mRetrofit.create(OMDbEndpointInterface.class);
        Call<OMDbSearchResponse> call = omDbApi.getResults(query, API_KEY);
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
                                omDbMovie.getPoster()
                        );
                        movieList.add(movie);
                    }

                    String totalResultsAsString = response.body().getTotalResults();

                    searchResult = new SearchResult(
                            SearchResultStatus.SUCCESSFUL,
                            Long.parseLong(totalResultsAsString),
                            movieList);

                } else if (response.isSuccessful() &&
                        response.body() != null &&
                        response.body().getOMDbMovies() == null) {
                    searchResult = new SearchResult(
                            SearchResultStatus.ERROR,
                            0,
                            null);
                } else {
                    searchResult = new SearchResult(
                            SearchResultStatus.ERROR,
                            0,
                            null);
                }

                callback.onResult(searchResult);
            }

            @Override
            public void onFailure(Call<OMDbSearchResponse> call,
                                  Throwable t) {
                callback.onResult(new SearchResult(SearchResultStatus.ERROR, 0, null));
            }
        });
    }
}
