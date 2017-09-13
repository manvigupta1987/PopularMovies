package com.example.manvi.movieappstage1.data.Source.remote;

import android.net.Uri;
import android.text.TextUtils;

import com.example.manvi.movieappstage1.BuildConfig;
import com.example.manvi.movieappstage1.Utils.NetworkUtils;
import com.example.manvi.movieappstage1.data.MovieData;
import com.example.manvi.movieappstage1.data.Reviews;
import com.example.manvi.movieappstage1.data.Trailer;

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
import java.util.Scanner;

/**
 * Created by manvi on 1/3/17.
 */

public class MovieResponseParser {

    private static final String TAG = MovieResponseParser.class.getSimpleName();
    private static final String BASE_URL = "https://api.themoviedb.org/3/movie";
    private final static String  API_KEY_PARAM = "api_key";
    private final static String LANGUAGE = "language";
    private final static String APPEND = "append_to_response";
    private final static String YOUTUBE_URL = "https://www.youtube.com/watch";
    private final static String video = "v";
    private final static String thumbnail = "http://img.youtube.com/vi/";
    private final static String videoReview = "videos,reviews";
    private final static String lang ="en-US";
    private final static String ThumbnailPath = "hqdefault.jpg";


    public static URL buildURL(String sortBy, int page)
    {
        Uri builtUri = Uri.parse(BASE_URL).buildUpon()
                .appendPath(sortBy)
                .appendQueryParameter("page",String.valueOf(page))
                .appendQueryParameter(API_KEY_PARAM, BuildConfig.API_KEY)
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
        BufferedReader br = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // Timeout for connection.connect() arbitrarily set to 3000ms.
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setReadTimeout(3000);
            // For this use case, set HTTP method to GET.
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setAllowUserInteraction(false);
            httpURLConnection.setUseCaches(false);
            InputStream error = httpURLConnection.getErrorStream();
            httpURLConnection.connect();

            int responseCode = httpURLConnection.getResponseCode();

            if(responseCode == HttpURLConnection.HTTP_OK) {
                br = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
                if (br != null) {
                    Scanner scanner = new Scanner(br);
                    scanner.useDelimiter("//A");

                    boolean hasNext = scanner.hasNext();
                    if (hasNext) {
                        return (scanner.next());
                    } else {
                        return null;
                    }
                }

            }else {
                return null;
            }
        }finally {
            if(br!=null){
                br.close();
            }
            if(httpURLConnection!=null) {
                httpURLConnection.disconnect();
            }
        }
        return null;
    }


    public static ArrayList<MovieData> getSimpleMovieDataFromJson(String movieJsonStr) throws JSONException
    {

        // If the JSON movie string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJsonStr)) {
            return null;
        }
                /* Movie information. Each Movie's info is an element of the "result" array */
        final String MD_RESULTS = "results";
        final String MD_TOTAL_RESULTS = "total_results";
        final String MD_TOTAL_PAGES = "total_pages";


        /* poster path is children of the "poster_path" object */
        final String MD_POSTER_PATH = "poster_path";

        /* overview of the movie */
        final String MD_OVERVIEW = "overview";
        /*release date of the movie */
        final String MD_RELEASE_DATE = "release_date";

        final String MD_TITLE = "title";
        final String MD_VOTE = "vote_average";

        final String MD_BACKDROP = "backdrop_path";
        final String MD_MovieID = "id";
        final String MD_LANG = "original_language";
        final String MD_Popularity = "popularity";
        final String MD_VOTE_COUNT = "vote_count";

        ArrayList<MovieData> movieItemList = new ArrayList<>();

        JSONObject MovieJsonObject = new JSONObject(movieJsonStr);

        JSONArray movieJsonArray = MovieJsonObject.getJSONArray(MD_RESULTS);

        for (int i = 0; i < movieJsonArray.length(); i++) {
           /* Get the JSON object representing the day */
            JSONObject movieItem = movieJsonArray.getJSONObject(i);
            String poster_path = movieItem.getString(MD_POSTER_PATH);
            String release_date = movieItem.getString(MD_RELEASE_DATE);
            String overview = movieItem.getString(MD_OVERVIEW);
            String title = movieItem.getString(MD_TITLE);
            Double vote = Double.parseDouble(movieItem.getString(MD_VOTE));
            int movieId = Integer.parseInt(movieItem.getString(MD_MovieID));
            String backDrop = movieItem.getString(MD_BACKDROP);
            String lang = movieItem.getString(MD_LANG);
            Double popularity = Double.parseDouble(movieItem.getString(MD_Popularity));
            int vote_Count = Integer.parseInt(movieItem.getString(MD_VOTE_COUNT));


            MovieData movieData = new MovieData(movieId,backDrop,lang,title,poster_path, overview, release_date, vote, popularity,vote_Count);
            movieItemList.add(movieData);
        }

        return movieItemList;
    }

    public static MovieData getTrailerReviewsFromJson(String movieJsonStr) throws JSONException
    {
        // If the JSON movie string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJsonStr)) {
            return null;
        }
                /* Movie information. Each Movie's info is an element of the "result" array */
        final String MD_VIDEOS = "videos";
        final String MD_RESULTS = "results";

        final String MD_ID ="id";

        /* poster path is children of the "poster_path" object */
        final String MD_KEY = "key";

        /* overview of the movie */
        final String MD_NAME = "name";
        /*release date of the movie */
        final String MD_SITE = "site";

        final String MD_SIZE = "size";
        final String MD_TYPE = "type";

        final String MD_REVIEWS = "reviews";
        final String MD_AUTHOR ="author";

        final String MD_CONTENT = "content";
        final String MD_URL = "url";

        ArrayList<Trailer> trailerList = null;

        JSONObject MovieJsonObject = new JSONObject(movieJsonStr);
        JSONObject videosJsonObject = MovieJsonObject.getJSONObject(MD_VIDEOS);

        JSONArray resultJsonArray = videosJsonObject.getJSONArray(MD_RESULTS);
        if(resultJsonArray!=null && resultJsonArray.length() != 0) {
            trailerList = new ArrayList<>();
            for (int i = 0; i < resultJsonArray.length(); i++) {
           /* Get the JSON object representing the day */
                JSONObject trailer = resultJsonArray.getJSONObject(i);
                String id = trailer.getString(MD_ID);
                String key = trailer.getString(MD_KEY);
                String site = trailer.getString(MD_SITE);

                URL url = null;
                URL thumbNailUrl = null;
                if (site.equals("YouTube")) {
                    url = buildYouTubeURLForTrailers(key);
                    thumbNailUrl = buildYouTubeThumbNailURLForTrailers(key);
                }

                Trailer trailer1 = new Trailer(id, url.toString(), thumbNailUrl.toString());
                trailerList.add(trailer1);
            }
        }

        ArrayList<Reviews> reviewList = null;
        JSONObject reviewsJsonObject = MovieJsonObject.getJSONObject(MD_REVIEWS);

        JSONArray reviewJsonArray = reviewsJsonObject.getJSONArray(MD_RESULTS);

        if(reviewJsonArray != null && reviewJsonArray.length()!=0) {
            reviewList = new ArrayList<>();
            for (int i = 0; i < reviewJsonArray.length(); i++) {
           /* Get the JSON object representing the day */
                JSONObject reviewObject = reviewJsonArray.getJSONObject(i);
                String id = reviewObject.getString(MD_ID);
                String author = reviewObject.getString(MD_AUTHOR);
                String content = reviewObject.getString(MD_CONTENT);

                Reviews review = new Reviews(id, author, content);
                reviewList.add(review);
            }
        }
        return (new MovieData(trailerList,reviewList));
    }
}
