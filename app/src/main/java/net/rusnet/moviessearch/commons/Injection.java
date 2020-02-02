package net.rusnet.moviessearch.commons;

import net.rusnet.moviessearch.commons.domain.usecase.AsyncUseCaseExecutor;
import net.rusnet.moviessearch.commons.utils.executors.DiskIOThreadExecutor;
import net.rusnet.moviessearch.commons.utils.executors.MainThreadExecutor;
import net.rusnet.moviessearch.search.data.source.MoviesRemoteDataSource;
import net.rusnet.moviessearch.search.data.source.OmdbApi;
import net.rusnet.moviessearch.search.domain.usecase.PerformSearch;
import net.rusnet.moviessearch.search.presentation.SearchContract;
import net.rusnet.moviessearch.search.presentation.SearchPresenter;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Injection {

    private static final String BASE_URL = "http://omdbapi.com/";

    private static MainThreadExecutor MAIN_THREAD_EXECUTOR_INSTANCE;
    private static DiskIOThreadExecutor DISK_IO_THREAD_EXECUTOR_INSTANCE;
    private static AsyncUseCaseExecutor ASYNC_USE_CASE_EXECUTOR_INSTANCE;
    private static OmdbApi OMDB_API_INSTANCE;
    private static MoviesRemoteDataSource MOVIES_REMOTE_DATA_SOURCE_INSTANCE;
    private static PerformSearch PERFORM_SEARCH_INSTANCE;
    private static SearchContract.Presenter SEARCH_PRESENTER_INSTANCE;

    public static MainThreadExecutor provideMainThreadExecutor() {
        if (MAIN_THREAD_EXECUTOR_INSTANCE == null) {
            MAIN_THREAD_EXECUTOR_INSTANCE = new MainThreadExecutor();
        }
        return MAIN_THREAD_EXECUTOR_INSTANCE;
    }

    public static DiskIOThreadExecutor provideDiskIOThreadExecutor() {
        if (DISK_IO_THREAD_EXECUTOR_INSTANCE == null) {
            DISK_IO_THREAD_EXECUTOR_INSTANCE = new DiskIOThreadExecutor();
        }
        return DISK_IO_THREAD_EXECUTOR_INSTANCE;
    }

    public static AsyncUseCaseExecutor provideAsyncUseCaseExecutor() {
        if (ASYNC_USE_CASE_EXECUTOR_INSTANCE == null) {
            ASYNC_USE_CASE_EXECUTOR_INSTANCE = new AsyncUseCaseExecutor(
                    provideMainThreadExecutor(),
                    provideDiskIOThreadExecutor()
            );
        }
        return ASYNC_USE_CASE_EXECUTOR_INSTANCE;
    }

    public static OmdbApi provideOmdbApi() {
        if (OMDB_API_INSTANCE == null) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
            OMDB_API_INSTANCE = retrofit.create(OmdbApi.class);
        }
        return OMDB_API_INSTANCE;
    }

    public static MoviesRemoteDataSource provideMoviesRemoteDataSource() {
        if (MOVIES_REMOTE_DATA_SOURCE_INSTANCE == null) {
            MOVIES_REMOTE_DATA_SOURCE_INSTANCE = new MoviesRemoteDataSource(
                    provideOmdbApi()
            );
        }
        return MOVIES_REMOTE_DATA_SOURCE_INSTANCE;
    }

    public static PerformSearch providePerformSearchUseCase() {
        if (PERFORM_SEARCH_INSTANCE == null) {
            PERFORM_SEARCH_INSTANCE = new PerformSearch(
                    provideMoviesRemoteDataSource()
            );
        }
        return PERFORM_SEARCH_INSTANCE;
    }

    public static SearchContract.Presenter provideSearchPresenter() {
        if (SEARCH_PRESENTER_INSTANCE == null) {
            SEARCH_PRESENTER_INSTANCE = new SearchPresenter(
                    providePerformSearchUseCase()
            );
        }
        return SEARCH_PRESENTER_INSTANCE;
    }

}
