package net.rusnet.moviessearch.commons.domain.usecase;

import androidx.annotation.NonNull;

public abstract class AsyncUseCase<Q, R> extends UseCase<Q, R> {

    private AsyncUseCaseExecutor mAsyncUseCaseExecutor;

    private AsyncUseCase() {
    }

    public AsyncUseCase(@NonNull AsyncUseCaseExecutor asyncUseCaseExecutor) {
        mAsyncUseCaseExecutor = asyncUseCaseExecutor;
    }

    public void execute(@NonNull final Q requestValues, @NonNull Callback<R> callback) {
        mAsyncUseCaseExecutor.execute(
                this,
                requestValues,
                callback);
    }

    @NonNull
    protected abstract R doInBackground(@NonNull Q requestValues);

}
