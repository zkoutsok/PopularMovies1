package com.example.androidzk.projectoneudacity;

import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.ImageView;

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

public class GridFragment extends Fragment {

    private enum moviesFilter {
        POPULAR,
        TOP_RATED
    }

    private final String LOGCAT_JSON_FETCH = "JSON FETCH";
    private final String LOGCAT_URI = "URI PARSING";
    private final String LOGCAT_JSON_PARSE = "JSON_PARSING";
    private final String KEY_WEB_QUERY ="api_key";
    private final String MOVIE_FILTER_POPULAR = "popular";
    private final String MOVIE_FILTER_TOP_RATED = "top_rated";

    public GridFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        FetchMoviesTask task = new FetchMoviesTask();
        task.execute(moviesFilter.POPULAR);
        View view= inflater.inflate(R.layout.fragment_grid, container, false);
        ImageView viewButton = (ImageView)view.findViewById(R.id.image_id);
        Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg").into(viewButton);
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

        private URL fetchMoviesDBAddress(moviesFilter ... params) throws MalformedURLException{

            if (params == null) return null;
            String movieFilter;
            switch (params[0]) {
                case TOP_RATED:
                    movieFilter = MOVIE_FILTER_TOP_RATED;
                    break;
                case POPULAR:
                    movieFilter = MOVIE_FILTER_POPULAR;
                    break;
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

            JSONObject initObject = new JSONObject(movieJsonString);
            JSONArray resultArray = initObject.getJSONArray(JSON_RESULTS);

            JSONObject resultOne = resultArray.getJSONObject(0);
            // JSONObject posterOne = resultOne.getJSONObject(JSON_POSTER);

            String posterPath = resultOne.getString(JSON_POSTER);

            final URL imageUrl = fetchMoviesDBImgAddress(posterPath);
            ImageView viewImg = (ImageView) getView().findViewById(R.id.image_id);
            BaseMovieInfo movieInfo = new BaseMovieInfo(viewImg);
            BaseMovieInfo [] movieInfoArray = new BaseMovieInfo[2];
            movieInfoArray[0] = movieInfo;
            Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185//zSouWWrySXshPCT4t3UKCQGayyo.jpg").into(viewImg);
            return movieInfoArray;
        }


        @Override
        protected void onPostExecute(BaseMovieInfo[] movies) {
            super.onPostExecute(movies);
            ImageView viewImg = (ImageView)getView().findViewById(R.id.image_id);
           //Picasso.with(getContext()).load("http://image.tmdb.org/t/p/w185//zSouWWrySXshPCT4t3UKCQGayyo.jpg").into(viewImg);
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



           // return new Movie[0];
        }
    }
}
