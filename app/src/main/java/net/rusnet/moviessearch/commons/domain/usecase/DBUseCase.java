package net.rusnet.moviessearch.commons.domain.usecase;

import androidx.annotation.NonNull;

public abstract class DBUseCase<Q, R> extends AsyncUseCase<Q, R> {

//    protected MovieDataSource mMovieDataSource;

    public DBUseCase(@NonNull AsyncUseCaseExecutor useCaseExecutor) {
        super(useCaseExecutor);
//        mMovieDataSource = movieDataSource;
    }

}
