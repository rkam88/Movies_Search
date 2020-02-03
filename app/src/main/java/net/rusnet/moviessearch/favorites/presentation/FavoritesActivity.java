package net.rusnet.moviessearch.favorites.presentation;

import android.app.ActionBar;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.moviessearch.R;
import net.rusnet.moviessearch.commons.Injection;
import net.rusnet.moviessearch.search.domain.model.Movie;
import net.rusnet.moviessearch.search.presentation.SearchActivity;

import java.util.List;

public class FavoritesActivity extends AppCompatActivity
        implements FavoritesContract.View,
        FavoritesMoviesAdapter.OnFavoritesButtonClickListener {

    private List<Movie> mMovieList;
    private RecyclerView mRecyclerView;
    private FavoritesMoviesAdapter mAdapter;
    private FavoritesContract.Presenter mPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //noinspection unchecked
        mMovieList = (List) getIntent().getSerializableExtra(SearchActivity.EXTRA_FAVORITE_MOVIES_LIST);

        ActionBar actionBar = getActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mRecyclerView = findViewById(R.id.favorites_recycler_view);
        mAdapter = new FavoritesMoviesAdapter(mMovieList, this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = Injection.provideFavoritesPresenter(getApplicationContext());
    }

    @Override
    public void onClick(@NonNull Movie movie) {
        mPresenter.deleteFromFavorites(movie);
    }
}
