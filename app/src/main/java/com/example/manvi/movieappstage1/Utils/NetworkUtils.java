package com.example.manvi.movieappstage1.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import com.example.manvi.movieappstage1.BuildConfig;
import com.example.manvi.movieappstage1.Model.GenreData;
import com.example.manvi.movieappstage1.Model.MovieData;
import com.example.manvi.movieappstage1.Model.MovieGenre;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by manvi on 1/3/17.
 */

public final class NetworkUtils {

    private final static String TAG = NetworkUtils.class.getSimpleName();

    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    private final static String  API_KEY_PARAM = "api_key";
    private final static String GENRELISTPATH = "https://api.themoviedb.org/3/genre/movie/list";
    private final static String LANGUAGE = "language";
    private final static String APPEND = "append_to_response";
    private final static String YOUTUBE_URL = "https://www.youtube.com/watch";
    private final static String video = "v";
    private final static String thumbnail = "http://img.youtube.com/vi/";
    private final static String videoReview = "videos,reviews";
    private final static String lang ="en-US";
    private final static String ThumbnailPath = "hqdefault.jpg";


    private static String jsonMoviesResponse;
    /**
     * Builds the URL for popular movies which is used to talk to the moviedb server.
     *
     * @return The URL to use to query the weather server.
     */
    public static URL buildURL(String sortBy)
    {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                          .appendPath(sortBy)
                          .appendQueryParameter(API_KEY_PARAM,BuildConfig.API_KEY)
                          .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }

    /*
        This function builds URL for genre list.
     */
    public static URL buildURLForGenre()
    {
        Uri builtUri = Uri.parse(GENRELISTPATH).buildUpon()
                .appendQueryParameter(API_KEY_PARAM,BuildConfig.API_KEY)
                .build();
        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }

    /*
        This function builds URL for genre list.reviews and trailers.
     */
    public static URL buildURLForReviewTrailers(String movieId)
    {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(movieId)
                .appendQueryParameter(API_KEY_PARAM,BuildConfig.API_KEY)
                .appendQueryParameter(LANGUAGE,lang)
                .appendQueryParameter(APPEND,videoReview)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }

    /*
    This function builds URL for the  you tube trailers.
     */
    public static URL buildYouTubeURLForTrailers(String key)
    {
        Uri builtUri = Uri.parse(YOUTUBE_URL).buildUpon()
                .appendQueryParameter(video,key)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }

    /*
        This function builds URL for the thumbnail image for the trailers.
     */
    public static URL buildYouTubeThumbNailURLForTrailers(String key)
    {
        Uri builtUri = Uri.parse(thumbnail).buildUpon()
                .appendPath(key)
                .appendPath(ThumbnailPath)
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url;
    }
    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response.
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException
    {
        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            httpURLConnection.setConnectTimeout(3000);
            // For this use case, set HTTP method to GET.
            httpURLConnection.setRequestMethod("GET");

            inputStream = httpURLConnection.getInputStream();
            if (inputStream != null) {
                Scanner scanner = new Scanner(inputStream);
                scanner.useDelimiter("//A");

                boolean hasNext = scanner.hasNext();
                if (hasNext) {
                    return (scanner.next());
                } else {
                    return null;
                }
            }
            else {
                return null;
            }
        }finally {
            // Close Stream and disconnect HTTPS connection.
            if(inputStream!=null)
            {
                inputStream.close();
            }
            if(httpURLConnection!=null) {
                httpURLConnection.disconnect();
            }
        }
    }

    /**Function checks for the network connection. If network connection exists
     * return true else false;
     * @param context
     * @return: true or false
     */
    public static boolean isNetworkConnectionAvailable(Context context)
    {
        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if(activeNetwork!=null && activeNetwork.isConnectedOrConnecting())
        {
            return true;
        }
        else{
            return false;
        }
    }

    //This function fetches movie data from the network.
    public static ArrayList<MovieData> fetchMovieInfoFromNetwork(String sortBy){
        URL url = NetworkUtils.buildURL(sortBy);
        try {
            jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(url);
            ArrayList<MovieData> simpleJsonMovieData = JsonMovieParserUtils.getSimpleMovieDataFromJson(jsonMoviesResponse);
            return simpleJsonMovieData;
            }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //This function fetches genre list from the network.
    public static ArrayList<GenreData> fetchGenreListFromNetwork(){
        URL url = NetworkUtils.buildURLForGenre();
        try {
            String jsonGenreResponse = NetworkUtils.getResponseFromHttpUrl(url);
            ArrayList<GenreData> simpleJsonGenreData = JsonMovieParserUtils.getSimpleGenreDataFromJson(jsonGenreResponse);
            return simpleJsonGenreData;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //This function fetches reviews and trailers from the network.
    public static MovieData fetchReviewsTrailersFromNetwork(String movieId){

        URL url = NetworkUtils.buildURLForReviewTrailers(movieId);
        try {
            String jsonGenreResponse = NetworkUtils.getResponseFromHttpUrl(url);
            MovieData reviewTrailerJsonData = JsonMovieParserUtils.getTrailerReviewsFromJson(jsonGenreResponse);
            return reviewTrailerJsonData;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //This function extracts the movie genre from the movie data fetched from the network.
    public static ArrayList<MovieGenre> extractMovieGenreFromNetwork(){
        try {
            ArrayList<MovieGenre> simpleJsonMovieGenreData = JsonMovieParserUtils.getSimpleMovieGenreFromJson(jsonMoviesResponse);
            return simpleJsonMovieGenreData;
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

