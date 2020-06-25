package nduati.soc.gads2020;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Scanner;
// This class is created as a helper class to retrieve data from the API (internet)
// helper class is a fancy word for we're trying to make our code neater by grouping related code together in this case this API call
class ApiUtil {
    public static final String QUERY_PARAMETER_KEY = "q";

    private ApiUtil(){}// making a constructor private means that this class cannot be instantiated

    private static final String TAG = "ApiUtil"; //this is a just a variable util created to be used when logging debug messages
    private static final String BASE_API_URL = "" +
            "https://www.googleapis.com/books/v1/volumes";
    public static final String KEY = "key";
    public static final String API_KEY = "INSERT_YOUR_API_KEY_IN_HERE";// insert your api key here
    // To get your own api key simply navigate to https://console.developers.google.com/apis/credentials and create one.

    /*This is the older method that was then updated to encompass the uri builder
    * public static URL BuildUrl(String title){
        String fullUrl = "?q="+title;
        URL url = null;
        try {
            url  = new URL(BASE_API_URL+fullUrl);// this converts the url you have of type String to type URL
        }catch (Exception e){
            Log.d(TAG, "BuildUrl: "+e);
        }
        return url; // this finally be either null or an actual URL
    }
    * */

    public static URL BuildUrl(String title){
        URL url = null;
        Uri uri = Uri.parse(BASE_API_URL).buildUpon()
                .appendQueryParameter(QUERY_PARAMETER_KEY, title)
//                .appendQueryParameter(KEY, API_KEY) /* commented out my api key as it dindt seem to work in this case*/
                .build();
//        remember to navigate to
        try {
            url = new URL(uri.toString()/*converting uri from type Uri to type string*/);
        }catch (Exception e){
            Log.d(TAG, "BuildUrl: "+e);
        }
        return url; // this finally be either null or an actual URL
    }

    public static String getJson(URL url) throws IOException{ // used throws exception same as using try catch ioexception
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();// must be called to connect to the api

        try {
            InputStream stream = connection.getInputStream(); // enables us to read data from connection i.e file/image/html
            Scanner scanner = new Scanner(stream); // convert stream to string/data buffering and encoding data to UTF16(android format)
            scanner.useDelimiter("\\A"); // regex code to say read all
            boolean hasData = scanner.hasNext(); // checks if there is content to be read in the scanner
            if(hasData){
                return scanner.next();
            }else {
                return null;
            }
        }catch (Exception e){
            Log.d(TAG, "getJson: "+e);// used for logging/printing debug errors to the logcat
            return null;
        }
        finally { // used when you want to have a piece of code run regardless of what happens in a try catch block
            connection.disconnect();// advisable to close your connection
        }

    }
}
