package nduati.soc.gads2020;

import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
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

    public static ArrayList<Book> getBooksFromJson(String json){
        // we're creating this method so that we can now use the pojo class just created I.E nduati/soc/gads2020/Book.java
        // to now return a list containing the book objects note that this will be retrieved from the api result
        // see the methdo takes in a string....this is the results we've been setting to our screen i.e the json_results
        // just incase you forgot an API is a resource provided by in most cases websites so that you can access the sites content in most cases for free
        // other than google books there are api's for maps, weather forecast, movies, news articles etc even facebook, twitter, youtube have APIS for you to use
        final String ID = "id";
        final String TITLE = "title";
        final String SUBTITLE = "subtitle";
        final String AUTHORS = "authors";
        final String PUBLISHERS = "publisher";
        final String PUBLISHED_DATE = "publishedDate";
        final String ITEMS = "items";
        final String VOLUMEINFO = "volumeInfo";
        // above are just constants that we will use to access the fields from the api results
        // final keyword implies that the variable is a constant and as such will not change
        // note that it is advisable to use UPPERCASE variable identifier names for constants but not required...just a good naming condition
        // the static keyword in a class implies that the property/method of a class can be accessed outside a class without the need of creating an object

        ArrayList<Book> books = new ArrayList<Book>(); // create an array list that is empty.
        try {
            JSONObject jsonBooks = new JSONObject(json); // to make the string usable we first need to create a json object from the string (api results)
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS); // now to retrieving the content in the api results
            // navigate to the Book.java class and if you look at the json result you'll notice that items is a keyword for
            // an array containing objects that contain book information..this is exactly what we are retreving right now..book information
            int numberOfBooks = arrayBooks.length();

            for(int i = 0; i < numberOfBooks; i++){
                Log.d(TAG, "getBooksFromJson: book position" + i + "books present "+ numberOfBooks);
                JSONObject bookJSON = arrayBooks.getJSONObject(i); // this will be a place holder for the current book we are iterating through in the arraybooks ..first book will be at position 0
                // and now that we are currently in our nth book at position (i)...we can retrieve information from it
                JSONObject volumeInfoJSON = bookJSON.getJSONObject(VOLUMEINFO); // this is an actual book now
                int authorNum = volumeInfoJSON.getJSONArray(AUTHORS).length(); // number of authors for this current book (rem we are at book (i))
                Log.d(TAG, "getBooksFromJson: author num " + authorNum);
                // remember that some books had more than one author and to retrieve the entire list we need to look through it hence the need to first get the number of authors from the authors array
                String[] authors = new String[authorNum]; // note that in Java when creating an array you are required to state the size of the array hence the authornum
                for(int j = 0; j < authorNum; j++){
                    authors[j] = volumeInfoJSON.getJSONArray(AUTHORS).get(j).toString();// retrieving teh author names and adding them to the new authors array we created
                    Log.d(TAG, "getBooksFromJson: names "+authors[j]);
                }
                Log.d(TAG, "getBooksFromJson: content "+bookJSON.getString(ID) +""+
                        volumeInfoJSON.getString(PUBLISHED_DATE));
                Book book = new Book(
                        bookJSON.getString(ID),
                        volumeInfoJSON.getString(TITLE),
                        (volumeInfoJSON.isNull(SUBTITLE)? "": volumeInfoJSON.getString(SUBTITLE)),// this is
                        authors,
                        volumeInfoJSON.getString(PUBLISHERS),
                        volumeInfoJSON.getString(PUBLISHED_DATE)
                        );// we have now officially created an entire book based on the POJO Book.java class we created.
                //remember that we are still in a loop and therefore this book will be as per the content on position (i)
                books.add(book); // add the book we just created to the books list array we created earlier.
            }
            Log.d(TAG, "getBooksFromJson: books sent "+books.size());
        }catch (JSONException e){
            e.printStackTrace();
        }
        return books;

    }
}
