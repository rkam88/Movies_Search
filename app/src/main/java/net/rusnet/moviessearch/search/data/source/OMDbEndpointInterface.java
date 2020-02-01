package net.rusnet.moviessearch.search.data.source;

import net.rusnet.moviessearch.search.data.model.OMDbSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OMDbEndpointInterface {

    @GET(".")
    Call<OMDbSearchResponse> getResults(@Query("s") String searchQuery,
                                        @Query("apikey") String apiKey);

}
