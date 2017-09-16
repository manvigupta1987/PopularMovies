package com.example.manvi.movieappstage1.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by manvi on 1/3/17.
 */

public final class NetworkUtils {

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

    public static String buildYouTubeThumbNailURLForTrailers(String key)
    {
        Uri builtUri = Uri.parse("http://img.youtube.com/vi/").buildUpon()
                .appendPath(key)
                .appendPath("hqdefault.jpg")
                .build();

        URL url = null;
        try{
            url = new URL(builtUri.toString());

        }catch(MalformedURLException e)
        {
            e.printStackTrace();
        }
        return url.toString();
    }
}

