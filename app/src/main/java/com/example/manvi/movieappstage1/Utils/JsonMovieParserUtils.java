package com.example.manvi.movieappstage1.Utils;

import android.text.TextUtils;

import com.example.manvi.movieappstage1.Model.GenreData;
import com.example.manvi.movieappstage1.Model.MovieData;
import com.example.manvi.movieappstage1.Model.MovieGenre;
import com.example.manvi.movieappstage1.Model.Reviews;
import com.example.manvi.movieappstage1.Model.Trailer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;

/**
 * Created by manvi on 1/3/17.
 */

public class JsonMovieParserUtils {

    private static final String TAG = JsonMovieParserUtils.class.getSimpleName();

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


    public static ArrayList<MovieGenre> getSimpleMovieGenreFromJson(String movieJsonStr) throws JSONException
    {

        // If the JSON movie string is empty or null, then return early.
        if (TextUtils.isEmpty(movieJsonStr)) {
            return null;
        }
                /* Movie information. Each Movie's info is an element of the "result" array */
        final String MD_RESULTS = "results";

        /* poster path is children of the "poster_path" object */
        final String MD_GENRE_IDS = "genre_ids";
        final String MD_MovieID = "id";

        JSONObject MovieJsonObject = new JSONObject(movieJsonStr);

        JSONArray movieJsonArray = MovieJsonObject.getJSONArray(MD_RESULTS);

        ArrayList<MovieGenre>  movieGenres = new ArrayList<>();

        for (int i = 0; i < movieJsonArray.length(); i++) {
           /* Get the JSON object representing the day */
            JSONObject movieJsonObj = movieJsonArray.getJSONObject(i);


            JSONArray genreArray = movieJsonObj.getJSONArray(MD_GENRE_IDS);
            for (int j = 0; j < genreArray.length(); j++) {
                long movieId = Long.parseLong(movieJsonObj.getString(MD_MovieID));
                Long genreId = genreArray.getLong(j);
                MovieGenre movieGenre = new MovieGenre(movieId, genreId);
                movieGenres.add(movieGenre);
            }
        }
        return movieGenres;
    }


    public static ArrayList<GenreData> getSimpleGenreDataFromJson(String genreJsonStr) throws JSONException
    {

        // If the JSON movie string is empty or null, then return early.
        if (TextUtils.isEmpty(genreJsonStr)) {
            return null;
        }
                /* Movie information. Each Movie's info is an element of the "result" array */
        final String MD_GENRES = "genres";

        /* poster path is children of the "poster_path" object */
        final String MD_GENRE_ID = "id";

        /* overview of the movie */
        final String MD_GENRE_NAME = "name";

        ArrayList<GenreData> genreList = new ArrayList<GenreData> () {
        };

        JSONObject genreJsonObject = new JSONObject(genreJsonStr);

        JSONArray genreJsonArray = genreJsonObject.getJSONArray(MD_GENRES);

        for (int i = 0; i < genreJsonArray.length(); i++) {
           /* Get the JSON object representing the day */
            JSONObject genreItem = genreJsonArray.getJSONObject(i);
            Integer genreId = Integer.parseInt(genreItem.getString(MD_GENRE_ID));
            String genreName = genreItem.getString(MD_GENRE_NAME);

            GenreData genreData = new GenreData(genreId,genreName);
            genreList.add(genreData);
        }
        return genreList;
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
                    url = NetworkUtils.buildYouTubeURLForTrailers(key);
                    thumbNailUrl = NetworkUtils.buildYouTubeThumbNailURLForTrailers(key);
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
