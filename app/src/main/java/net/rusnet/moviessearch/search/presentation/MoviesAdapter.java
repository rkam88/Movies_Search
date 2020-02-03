package net.rusnet.moviessearch.search.presentation;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import net.rusnet.moviessearch.R;
import net.rusnet.moviessearch.search.domain.model.Movie;

import java.util.List;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.ViewHolder> {

    private OnScrollListener mOnScrollListener;

    interface OnScrollListener {

        void onScroll(int pageToLoad, @NonNull String searchQuery);
    }

    public void setOnScrollListener(OnScrollListener onScrollListener) {
        mOnScrollListener = onScrollListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        TextView mTitleTextView;
        ImageView mPosterImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mTitleTextView = itemView.findViewById(R.id.text_view_title);
            mPosterImageView = itemView.findViewById(R.id.image_view_poster);
        }

    }

    private static final String EMPTY_STRING = "";
    private static final int LOAD_MORE_OFFSET = 5;
    private static final int NEXT_PAGE = 1;
    private List<Movie> mMovieList;
    private long mTotalResults;
    private int mMoviesPerPage;
    private String mSearchQuery;

    public MoviesAdapter(@NonNull List<Movie> movieList,
                         @NonNull String searchQuery,
                         long totalResults) {
        mMovieList = movieList;
        mTotalResults = totalResults;
        mSearchQuery = searchQuery;
    }

    @NonNull
    public List<Movie> getMovieList() {
        return mMovieList;
    }

    public long getTotalResults() {
        return mTotalResults;
    }

    public void setMovieList(@NonNull List<Movie> movieList,
                             @NonNull String searchQuery,
                             long totalResults) {
        mMovieList = movieList;
        mTotalResults = totalResults;
        mSearchQuery = searchQuery;
        mMoviesPerPage = mMovieList.size();
    }

    public void updateMovieList(@NonNull List<Movie> movieList) {
        mMovieList.addAll(movieList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_movie, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movie movie = mMovieList.get(position);

        TextView titleTextView = holder.mTitleTextView;
        ImageView posterImageView = holder.mPosterImageView;

        String fullTitle = String.format(titleTextView.getContext().getString(R.string.movie_title),
                movie.getTitle(),
                movie.getYear());
        titleTextView.setText(fullTitle);

        String imageUri = movie.getPosterURL();
        if (imageUri.equals(EMPTY_STRING)) {
            posterImageView.setImageResource(R.drawable.ic_no_poster_black_48dp);
        } else {
            Picasso.with(titleTextView.getContext())
                    .load(imageUri)
                    .fit()
                    .centerCrop()
                    .placeholder(R.drawable.ic_search_black_24dp)
                    .error(R.drawable.ic_error_black_48dp)
                    .into(posterImageView);
        }

        if (mMovieList.size() != mTotalResults &&
                position == mMovieList.size() - LOAD_MORE_OFFSET) {
            int pageToLoad = mMovieList.size() / mMoviesPerPage + NEXT_PAGE;
            mOnScrollListener.onScroll(pageToLoad, mSearchQuery);
        }
    }

    @Override
    public int getItemCount() {
        return mMovieList.size();
    }

}
