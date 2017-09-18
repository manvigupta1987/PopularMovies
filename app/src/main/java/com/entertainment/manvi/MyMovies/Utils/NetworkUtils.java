package com.entertainment.manvi.MyMovies.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public final class NetworkUtils {

    public static boolean isNetworkConnectionAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return !(networkInfo == null || !networkInfo.isConnected() || !networkInfo.isAvailable());
    }

    public static String buildYouTubeThumbNailURLForTrailers(String key)
    {
        Uri builtUri = Uri.parse("http://img.youtube.com/vi/").buildUpon()
                .appendPath(key)
                .appendPath("hqdefault.jpg")
                .build();

        URL url = null;
        try{
            if(builtUri!=null) {
                url = new URL(builtUri.toString());
            }

        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url != null ? url.toString() : null;
    }
}

