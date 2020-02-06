package net.rusnet.moviessearch.favorites.presentation;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
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
    private FrameLayout mInfoMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        //noinspection unchecked
        mMovieList = (List) getIntent().getSerializableExtra(SearchActivity.EXTRA_FAVORITE_MOVIES_LIST);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mInfoMessage = findViewById(R.id.message_frame_layout);
        if (mMovieList.isEmpty()) mInfoMessage.setVisibility(View.VISIBLE);

        mRecyclerView = findViewById(R.id.favorites_recycler_view);
        mAdapter = new FavoritesMoviesAdapter(mMovieList, this);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = Injection.provideFavoritesPresenter(getApplicationContext());
    }

    @Override
    public void onClick(@NonNull Movie movie) {
        if (mMovieList.isEmpty()) mInfoMessage.setVisibility(View.VISIBLE);
        mPresenter.deleteFromFavorites(movie);
        setResult(RESULT_OK);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

}
