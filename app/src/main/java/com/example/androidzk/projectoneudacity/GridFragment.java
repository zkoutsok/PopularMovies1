package com.example.androidzk.projectoneudacity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Arrays;

public class GridFragment extends Fragment {

    MovieInfoAdapter movieInfoAdapter;

    /** The following parameter is used as key in order for this fragment
     * to obtain the movies sort filter applied by settings.
     * This filter is forwarded from main activity.
     */
    private static final String MOVIE_FILTER = "movieFilter";
    private MovieFactory.MoviesFilter filter;

    public GridFragment() {
        // Required empty public constructor
    }

    public static GridFragment newInstance(MovieFactory.MoviesFilter filter) {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();
        args.putInt(MOVIE_FILTER, filter.getValue() );
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           filter  = MovieFactory.MoviesFilter.fromOrdinal(
                   getArguments().getInt(MOVIE_FILTER));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_grid, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.movie_grid);
        movieInfoAdapter = new MovieInfoAdapter(getActivity(), new ArrayList<BasicMovieInfo>());
        gridView.setAdapter(movieInfoAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(), DetailActivity.class);
                intent.putExtra(DetailFragment.MOVIE_ID, Long.valueOf(((ImageView)view).getTag().toString()));
                startActivity(intent);
            }
        });
        FetchMoviesTask task = new FetchMoviesTask();
        task.execute(this.filter);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /** The following task is used to retrieve through network connectivity and JSON parsing
     * the complete web addresses for the movie posters used in the gridView of
     * the main activity.
     */
    private class FetchMoviesTask extends AsyncTask<MovieFactory.MoviesFilter, Void, BasicMovieInfo[]> {

        @Override
        protected void onPostExecute(BasicMovieInfo[] movies) {
            super.onPostExecute(movies);
            movieInfoAdapter.clear();
            movieInfoAdapter.addAll(new ArrayList<BasicMovieInfo>(Arrays.asList(movies)));
        }

        @Override
        protected BasicMovieInfo[] doInBackground(MovieFactory.MoviesFilter... params) {
            return MovieFactory.getBasicMovieInfo(params);
        }
    }
}
