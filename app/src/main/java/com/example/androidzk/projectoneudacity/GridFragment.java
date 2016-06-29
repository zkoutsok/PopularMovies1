package com.example.androidzk.projectoneudacity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.*;
import android.support.v4.app.Fragment;
import android.support.v7.appcompat.*;
import android.support.v7.appcompat.BuildConfig;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridFragment extends Fragment {

    private enum moviesFilter {
        POPULAR,
        TOP_RATED,
        SINGLE
    }

    private final String LOGCAT_JSON_FETCH = "JSON FETCH";
    private final String LOGCAT_URI = "URI PARSING";
    private final String LOGCAT_JSON_PARSE = "JSON_PARSING";
    private final String KEY_WEB_QUERY ="api_key";
    private final String MOVIE_FILTER_POPULAR = "popular";
    private final String MOVIE_FILTER_TOP_RATED = "top_rated";

    private ArrayList<BaseMovieInfo> movieInfo;
    MovieInfoAdapter movieInfoAdapter;

    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_grid, container, false);
        GridView gridView = (GridView) view.findViewById(R.id.movie_grid);
        movieInfoAdapter = new MovieInfoAdapter(getActivity(), new ArrayList<BaseMovieInfo>());
        gridView.setAdapter(movieInfoAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getContext(),((ImageView)view).getTag().toString(),Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(getContext(), DetailActivity.class);
                //intent.putExtra()
                //Edo start to allo activity, to opoio sto onCreateView tha fernei dedomena
                //me vasi auto to title.
            }
        });
        FetchMoviesTask task = new FetchMoviesTask();
        task.execute(moviesFilter.POPULAR);
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

    private class FetchMoviesTask extends AsyncTask<moviesFilter, Void, BaseMovieInfo[]> {

        String getMovieJSONString(URL url) throws IOException{

            if (url ==null) return null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String movieJsonString = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    movieJsonString = null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    movieJsonString = null;
                }
                movieJsonString = buffer.toString();
            } finally{
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    reader.close();
                }
            }
            return movieJsonString;
        }

        private final URL fetchMoviesDBImgAddress(String posterPath) throws MalformedURLException{

            if (posterPath == null) return null;

            final String imgSize = "w185";

            Uri builder = Uri.parse(
                    com.example.androidzk.projectoneudacity.BuildConfig.MOVIESDB_IMG_BASE_ADDRESS);

            Uri.Builder uriBuilder = builder
                    .buildUpon().
                    appendPath(imgSize).
                    appendEncodedPath(posterPath);

            return new URL(uriBuilder.build().toString());
        }

        private URL fetchMoviesDBAddress(moviesFilter ...params) throws MalformedURLException {
            return fetchMoviesDBAddress(0,params);
        }


        private URL fetchMoviesDBAddress(long id, moviesFilter ... params) throws MalformedURLException{

            if (params == null) return null;
            String movieFilter;
            switch (params[0]) {
                case TOP_RATED:
                    movieFilter = MOVIE_FILTER_TOP_RATED;
                    break;
                case POPULAR:
                    movieFilter = MOVIE_FILTER_POPULAR;
                    break;
                case SINGLE:
                    movieFilter = String.valueOf(id);
                default:
                    Log.e(LOGCAT_URI, "Movie filter not available");
                    throw new MalformedURLException();
            }
            Uri builder = Uri.parse(
                    com.example.androidzk.projectoneudacity.BuildConfig.MOVIESDB_BASE_ADDRESS);

            Uri.Builder uriBuilder = builder.
                    buildUpon().
                    appendPath(movieFilter).
                    appendQueryParameter(KEY_WEB_QUERY,
                    com.example.androidzk.projectoneudacity.BuildConfig.MOVIESDB_API_KEY);
            return new URL(uriBuilder.build().toString());

        }

        // We populate the BaseMovieInfo Objects.
        private BaseMovieInfo[] parseJSONString (String movieJsonString)
                throws JSONException, MalformedURLException {

            if (movieJsonString == null) return null;

            final String JSON_RESULTS = "results";
            final String JSON_POSTER = "poster_path";
            final String JSON_ID = "id";

            JSONObject initObject = new JSONObject(movieJsonString);
            JSONArray resultArray = initObject.getJSONArray(JSON_RESULTS);

            movieInfo = new ArrayList<BaseMovieInfo>();

            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject resultOne = resultArray.getJSONObject(i);

                String posterPath = resultOne.getString(JSON_POSTER);
                URL imageUrl = fetchMoviesDBImgAddress(posterPath);

                long id = resultOne.getLong(JSON_ID);
                movieInfo.add(new BaseMovieInfo(imageUrl.toString(), id));
            }
            return movieInfo.toArray(new BaseMovieInfo[movieInfo.size()]);
        }

        @Override
        protected void onPostExecute(BaseMovieInfo[] movies) {
            super.onPostExecute(movies);
            movieInfoAdapter.clear();
            movieInfoAdapter.addAll(new ArrayList<BaseMovieInfo>(Arrays.asList(movies)));
        }

        @Override
        protected BaseMovieInfo[] doInBackground(moviesFilter ... params) {

          // Fetch the movies db web address
            if (params == null) return null;
            URL url = null;
            String moviesJsonString = null;
            try {
                url = fetchMoviesDBAddress(params);
                moviesJsonString = getMovieJSONString(url);
                return parseJSONString(moviesJsonString);
            } catch (MalformedURLException e) {
                Log.e(LOGCAT_URI, "Error parsing the url address", e);
                e.printStackTrace();
                return null;
            } catch (IOException e) {
                Log.e(LOGCAT_JSON_FETCH, "Error fetching the JSON string", e);
                e.printStackTrace();
                return null;
            } catch (JSONException e) {
                Log.e(LOGCAT_JSON_PARSE, "Error parsing the JSON string", e);
                e.printStackTrace();
                return null;
            }
        }
    }
}
