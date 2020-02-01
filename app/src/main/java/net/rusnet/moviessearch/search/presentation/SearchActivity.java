package net.rusnet.moviessearch.search.presentation;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import net.rusnet.moviessearch.R;
import net.rusnet.moviessearch.commons.Injection;
import net.rusnet.moviessearch.search.domain.model.Movie;

import java.util.List;

public class SearchActivity extends AppCompatActivity implements SearchContract.View {

    private SearchContract.Presenter mPresenter;

    private EditText mSearchEditText;
    private ImageButton mSearchButton;
    private TextView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        mPresenter = new SearchPresenter(this,
                Injection.providePerformSearchUseCase());

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

    }

    @Override
    public void showMovies(List<Movie> movieList) {
        //todo: implement
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

