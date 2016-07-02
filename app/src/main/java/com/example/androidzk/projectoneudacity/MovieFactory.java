package com.example.androidzk.projectoneudacity;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by androidZK on 6/29/2016.
 *
 * This class is used in order for the different fragments to obtain different movie Objects
 * based on the sorting filter that they will apply.
 *
 * All networking, JSON parsing for the different objects are applied within this class.
 *
 */
public class MovieFactory{

    public static enum MoviesFilter {
        POPULAR(0),
        TOP_RATED(1),
        SINGLE(2);

        private final int value;
        private MoviesFilter(int value) {this.value = value;}
        private static MoviesFilter[] allValues = values();
        public static MoviesFilter fromOrdinal(int n) {return allValues[n];}

        public int getValue() {return value;}

    }

    public static ArrayList<BasicMovieInfo> movies;

    private static final String JSON_RESULTS = "results";
    private static final String JSON_POSTER = "poster_path";
    private static final String JSON_ID = "id";
    private static final String JSON_OVERVIEW = "overview";
    private static final String JSON_RATING = "vote_average";
    private static final String JSON_RELEASE_DATE = "release_date";
    private static final String JSON_TITLE = "title";


    private static final String LOGCAT_JSON_FETCH = "JSON FETCH";
    private static final String LOGCAT_URI = "URI PARSING";
    private static final String LOGCAT_JSON_PARSE = "JSON_PARSING";
    private static final String KEY_WEB_QUERY ="api_key";
    private static final String MOVIE_FILTER_POPULAR = "popular";
    private static final String MOVIE_FILTER_TOP_RATED = "top_rated";

    private static String getMovieJSONString(URL url) throws IOException {

        if (url == null) return null;

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
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                reader.close();
            }
        }
        return movieJsonString;
    }

    private static final URL fetchMoviesDBImgAddress(String posterPath) throws MalformedURLException {

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

    private static URL fetchMoviesDBAddress(MoviesFilter ...params) throws MalformedURLException {
        return fetchMoviesDBAddress(0,params);
    }

    private static URL fetchMoviesDBAddress(long id, MoviesFilter ... params) throws MalformedURLException{

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
                break;
            default:
                Log.e(LOGCAT_URI, "DetailMovieInfo filter not available");
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

    // We populate the BasicMovieInfo Objects.
    private static BasicMovieInfo[] parseJSONString (String movieJsonString)
            throws JSONException, MalformedURLException {

        if (movieJsonString == null) return null;

        JSONObject initObject = new JSONObject(movieJsonString);
        JSONArray resultArray = initObject.getJSONArray(JSON_RESULTS);

        ArrayList<BasicMovieInfo> movieInfo = new ArrayList<BasicMovieInfo>();

        for (int i = 0; i < resultArray.length(); i++) {
            JSONObject resultOne = resultArray.getJSONObject(i);

            String posterPath = resultOne.getString(JSON_POSTER);
            URL imageUrl = fetchMoviesDBImgAddress(posterPath);

            long id = resultOne.getLong(JSON_ID);
            movieInfo.add(new BasicMovieInfo(imageUrl.toString(), id));
        }
        return movieInfo.toArray(new BasicMovieInfo[movieInfo.size()]);
    }

    private static DetailMovieInfo parseDetailJSONString (String movieJsonString)
        throws JSONException, MalformedURLException {

        if (movieJsonString == null) return null;

        JSONObject initObject = new JSONObject(movieJsonString);
        String movieSynopsis = initObject.getString(JSON_OVERVIEW);
        float movieRating = (float)initObject.getDouble(JSON_RATING);
        String movieReleaseDate = initObject.getString(JSON_RELEASE_DATE);
        String posterPath = initObject.getString(JSON_POSTER);
        URL imageUrl = fetchMoviesDBImgAddress(posterPath);
        long movieID = initObject.getLong(JSON_ID);
        String movieTitle = initObject.getString(JSON_TITLE);

        return new DetailMovieInfo(movieTitle, movieSynopsis, movieRating, movieReleaseDate,
                               imageUrl.toString(), movieID);
    }


    public static DetailMovieInfo getDetailMovieInfo(long id) {
        URL url;
        String movieJsonString = null;
        try {
            url = fetchMoviesDBAddress(id, MoviesFilter.SINGLE);
            movieJsonString = getMovieJSONString(url);
            return parseDetailJSONString(movieJsonString);
        } catch (MalformedURLException e) {
            Log.e(LOGCAT_URI, "Error parsing the url movie address", e);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e(LOGCAT_JSON_FETCH, "Error fetching the JSON movie string", e);
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            Log.e(LOGCAT_JSON_PARSE, "Error parsing the JSON movie string", e);
            e.printStackTrace();
            return null;
        }

    }

    public static BasicMovieInfo[] getBasicMovieInfo(MoviesFilter... filter) {
        URL url = null;
        String moviesJsonString = null;
        try {
            url = fetchMoviesDBAddress(filter);
            moviesJsonString = getMovieJSONString(url);
            return parseJSONString(moviesJsonString);
        } catch (MalformedURLException e) {
            Log.e(LOGCAT_URI, "Error parsing the group movies url address", e);
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            Log.e(LOGCAT_JSON_FETCH, "Error fetching the group movies JSON string", e);
            e.printStackTrace();
            return null;
        } catch (JSONException e) {
            Log.e(LOGCAT_JSON_PARSE, "Error parsing the group movies JSON string", e);
            e.printStackTrace();
            return null;
        }
    }
}
