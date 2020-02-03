package net.rusnet.moviessearch.search.presentation;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
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
import net.rusnet.moviessearch.search.domain.model.Movie;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity
        implements SearchContract.View, MoviesAdapter.OnScrollListener {

    private static final int STARTING_SCROLL_POSITION = 0;
    private static final String EMPTY_STRING = "";
    private static final String KEY_MOVIE_LIST = "MOVIE_LIST";
    private static final String KEY_TOTAL_RESULTS = "KEY_TOTAL_RESULTS";
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

        mPresenter = Injection.provideSearchPresenter();
        mPresenter.setView(this);

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

        mRecyclerView = findViewById(R.id.recycler_view);

        List<Movie> movieList = new ArrayList<>();
        long totalResults = ZERO;
        if (savedInstanceState != null && savedInstanceState.containsKey(KEY_MOVIE_LIST)) {
            //noinspection unchecked
            movieList = (ArrayList<Movie>) savedInstanceState.getSerializable(KEY_MOVIE_LIST);
            totalResults = savedInstanceState.getLong(KEY_TOTAL_RESULTS);
        }
        if (movieList == null) movieList = new ArrayList<>();
        mMoviesAdapter = new MoviesAdapter(movieList, EMPTY_STRING, totalResults);
        mMoviesAdapter.setOnScrollListener(this);

        mRecyclerView.setAdapter(mMoviesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putSerializable(KEY_MOVIE_LIST,
                new ArrayList<>(mMoviesAdapter.getMovieList()));
        outState.putLong(KEY_TOTAL_RESULTS, mMoviesAdapter.getTotalResults());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void showMovies(@NonNull List<Movie> movieList,
                           @NonNull String searchQuery,
                           long totalResults) {
        mMoviesAdapter.setMovieList(movieList, searchQuery, totalResults);
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
    public void onScroll(int pageToLoad, @NonNull String searchQuery) {
        mPresenter.loadResultsPage(pageToLoad, searchQuery);
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

