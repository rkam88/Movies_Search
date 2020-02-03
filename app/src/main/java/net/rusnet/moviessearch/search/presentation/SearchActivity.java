package net.rusnet.moviessearch.search.presentation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.rusnet.moviessearch.R;
import net.rusnet.moviessearch.commons.Injection;
import net.rusnet.moviessearch.favorites.presentation.FavoritesActivity;
import net.rusnet.moviessearch.search.domain.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements SearchContract.View,
        MoviesAdapter.OnScrollListener,
        MoviesAdapter.OnFavoritesButtonClickListener {

    public static final String EXTRA_FAVORITE_MOVIES_LIST = "EXTRA_FAVORITE_MOVIES_LIST";
    private static final int STARTING_SCROLL_POSITION = 0;
    private static final String EMPTY_STRING = "";
    private static final String KEY_MOVIE_LIST = "MOVIE_LIST";
    private static final String KEY_TOTAL_RESULTS = "KEY_TOTAL_RESULTS";
    private static final String KEY_MOVIES_PER_PAGE = "KEY_MOVIES_PER_PAGE";
    private static final String KEY_SEARCH_QUERY = "KEY_SEARCH_QUERY";
    private static final String KEY_FAVORITE_MOVIES = "KEY_FAVORITE_MOVIES";
    private static final int ZERO = 0;

    private SearchContract.Presenter mPresenter;
    private EditText mSearchEditText;
    private ImageButton mSearchButton;
    private RecyclerView mRecyclerView;
    private MoviesAdapter mMoviesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        initPresenter();

        initViews();

        initRecyclerView(savedInstanceState);

        loadFavoriteMovies(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_MOVIE_LIST,
                new ArrayList<>(mMoviesAdapter.getMovieList()));
        outState.putLong(KEY_TOTAL_RESULTS, mMoviesAdapter.getTotalResults());
        outState.putInt(KEY_MOVIES_PER_PAGE, mMoviesAdapter.getMoviesPerPage());
        outState.putString(KEY_SEARCH_QUERY, mMoviesAdapter.getSearchQuery());
        outState.putSerializable(KEY_FAVORITE_MOVIES,
                new ArrayList<>(mMoviesAdapter.getFavoriteMovies()));
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showMovies(@NonNull List<Movie> movieList,
                           @NonNull String searchQuery,
                           long totalResults) {
        mMoviesAdapter.setMovieList(movieList);
        mMoviesAdapter.setSearchQuery(searchQuery);
        mMoviesAdapter.setTotalResults(totalResults);
        mMoviesAdapter.notifyDataSetChanged();
        RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager != null) {
            layoutManager.scrollToPosition(STARTING_SCROLL_POSITION);
        }
    }

    @Override
    public void updateMovies(@NonNull List<Movie> movieList) {
        mMoviesAdapter.updateMovieList(movieList);
        mMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showRequestErrorMessage() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showNetworkErrorMessage() {
        Toast.makeText(this, R.string.network_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showOtherErrorMessage() {
        Toast.makeText(this, R.string.other_error_message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateFavoriteMovies(@NonNull List<Movie> movieList) {
        mMoviesAdapter.setFavoriteMovies(movieList);
        mMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void onScroll(int pageToLoad, @NonNull String searchQuery) {
        mPresenter.loadResultsPage(pageToLoad, searchQuery);
    }

    @Override
    public void onClick(@NonNull Movie movie) {
        mPresenter.changeMovieFavoriteStatus(movie);
    }

    public void onFavoritesClick(MenuItem item) {
        if (item.getItemId() == R.id.menu_favorites) {
            Intent intent = new Intent(this, FavoritesActivity.class);
            intent.putExtra(EXTRA_FAVORITE_MOVIES_LIST, new ArrayList<>(mMoviesAdapter.getFavoriteMovies()));
            startActivity(intent);
        }
    }

    private void initPresenter() {
        mPresenter = Injection.provideSearchPresenter(getApplicationContext());
        mPresenter.setView(this);
    }

    private void initViews() {
        mSearchEditText = findViewById(R.id.edit_text_search);
        mSearchButton = findViewById(R.id.button_search);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.performSearch(mSearchEditText.getText().toString());
                hideKeyboard();
            }
        });
        mSearchEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    mSearchButton.callOnClick();
                    return true;
                }
                return false;
            }
        });
    }

    private void initRecyclerView(Bundle savedInstanceState) {
        mRecyclerView = findViewById(R.id.recycler_view);

        List<Movie> movieList = new ArrayList<>();
        String searchQuery = EMPTY_STRING;
        long totalResults = ZERO;
        int moviesPerPage = ZERO;
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIE_LIST)) {
            //noinspection unchecked
            movieList = (ArrayList<Movie>) savedInstanceState.getSerializable(KEY_MOVIE_LIST);
            searchQuery = savedInstanceState.getString(KEY_SEARCH_QUERY);
            totalResults = savedInstanceState.getLong(KEY_TOTAL_RESULTS);
            moviesPerPage = savedInstanceState.getInt(KEY_MOVIES_PER_PAGE);
        }
        if (movieList == null) movieList = new ArrayList<>();
        if (searchQuery == null) searchQuery = EMPTY_STRING;
        mMoviesAdapter = new MoviesAdapter(movieList, searchQuery, totalResults, moviesPerPage);
        mMoviesAdapter.setOnScrollListener(this);
        mMoviesAdapter.setOnFavoritesButtonClickListener(this);

        mRecyclerView.setAdapter(mMoviesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadFavoriteMovies(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_FAVORITE_MOVIES)) {
            //noinspection unchecked
            List<Movie> favoriteMovies = (ArrayList<Movie>) savedInstanceState.getSerializable(KEY_FAVORITE_MOVIES);
            if (favoriteMovies == null) favoriteMovies = new ArrayList<>();
            updateFavoriteMovies(favoriteMovies);
        } else {
            mPresenter.loadFavoriteMovies();
        }
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = findViewById(R.id.constraint_layout);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}

