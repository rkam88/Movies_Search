package net.rusnet.moviessearch.commons.domain.usecase;

import androidx.annotation.NonNull;

public abstract class UseCase<Q, R> {

    public abstract void execute(@NonNull final Q requestValues, @NonNull Callback<R> callback);

    public interface Callback<R> {
        void onResult(@NonNull R result);
    }
}
