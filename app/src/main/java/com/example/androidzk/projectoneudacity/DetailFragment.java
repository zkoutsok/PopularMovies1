package com.example.androidzk.projectoneudacity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DetailFragment extends Fragment {
    public static final String MOVIE_ID = "MOVIE_ID";
    private static final String ratingPostfix = "/10";
    private long movieID;

    public DetailFragment() {
        // Required empty public constructor
    }

    public static DetailFragment newInstance(long movieID) {
        DetailFragment fragment = new DetailFragment();
        Bundle args = new Bundle();
        args.putLong(MOVIE_ID, movieID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            movieID = getArguments().getLong(MOVIE_ID);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        FetchDetailMovieTask task = new FetchDetailMovieTask();
        task.execute(movieID);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class FetchDetailMovieTask extends AsyncTask<Long, Void, DetailMovieInfo> {

        @Override
        protected void onPostExecute(DetailMovieInfo movie) {
            super.onPostExecute(movie);
            TextView title = (TextView)getView().findViewById(R.id.title_id);
            title.setText( movie.getMovieTitle());
            TextView synopsis = (TextView)getView().findViewById(R.id.overview_id);
            synopsis.setText(movie.getMovieSynopsis());
            TextView rating = (TextView) getView().findViewById(R.id.rating_id);
            rating.setText(String.valueOf(movie.getMovieRating()) + ratingPostfix);
            TextView releaseDate = (TextView) getView().findViewById(R.id.release_date_id);
            releaseDate.setText(movie.getMovieReleaseDate());

            ImageView img = (ImageView)getView().findViewById(R.id.image_id);
            Picasso.with(getContext()).load(movie.getMoviePosterAddress()).into(img);
        }

        @Override
        protected DetailMovieInfo doInBackground(Long... id) {
            return MovieFactory.getDetailMovieInfo(id[0]);
        }
    }
}
