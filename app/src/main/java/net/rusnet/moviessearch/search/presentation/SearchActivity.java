package net.rusnet.moviessearch.search.presentation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
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

public class SearchActivity extends AppCompatActivity implements SearchContract.View {

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

        mRecyclerView = findViewById(R.id.recycler_view);
        mMoviesAdapter = new MoviesAdapter(new ArrayList<Movie>());
        mRecyclerView.setAdapter(mMoviesAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    public void showMovies(@NonNull List<Movie> movieList) {
        mMoviesAdapter.setMovieList(movieList);
        mMoviesAdapter.notifyDataSetChanged();
    }

    @Override
    public void showErrorMessage() {
        Toast.makeText(this, R.string.error_message, Toast.LENGTH_SHORT).show();
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

