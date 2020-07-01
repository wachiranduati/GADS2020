package nduati.soc.gads2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private static final String TAG = "MainActivity";
    private TextView tvError;
    private ProgressBar mLoadingProgress;
    private RecyclerView rvBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // finds the TextView Widget in activity_main layout file under id tvResponse and binds it to the variable
        rvBooks = findViewById(R.id.rv_books);
        mLoadingProgress = findViewById(R.id.pb_loading);
        tvError = findViewById(R.id.tv_error);
        try { // remember our function simply throws the error
            URL bookURL = ApiUtil.BuildUrl("cooking"); // calling the builder method we created in our APIUtil class which would append the string to the base url creating a complete url
            new BooksQueryTask().execute(bookURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(booksLayoutManager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.book_list_menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        searchView.setOnQueryTextListener(this);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        try {
            URL bookUrl = ApiUtil.BuildUrl(query);
            new BooksQueryTask().execute(bookUrl);
        }catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String>{// async task is a class that enables us to work in the background thread, taking thread work off the main thread

        @Override
        protected String doInBackground(URL... urls) { // this is the method that must be implemented in the class. This method does the actual work
            URL searchURL = urls[0]; // retrieve the very first item passed as a param..note the ..urls means you can pass multiple urls to the class
            String result = null;
            try {
                result = ApiUtil.getJson(searchURL);
            }catch (Exception e){
                Log.d(TAG, "doInBackground: "+e);
            }
            return result;
        }

        @Override
        protected void onPostExecute(String result) {// called once the code in doinbackground has completed
            mLoadingProgress.setVisibility(View.INVISIBLE);
            if (result == null){ // api call result empty means we ran into an error so
                tvError.setVisibility(View.VISIBLE);  // show error textView
                rvBooks.setVisibility(View.INVISIBLE); // hide result textView
            }else{ // api call result is not empty so success
                tvError.setVisibility(View.INVISIBLE); // hide error textView
                rvBooks.setVisibility(View.VISIBLE); // show textview
            }
            ArrayList<Book> books = ApiUtil.getBooksFromJson(result);
            Log.d(TAG, "onPostExecute: books returned" + books.size());
            String resultString = "";
//            for(Book book : books){ // this is a different way of writing a for loop where Book book represents each item you intent to loop
//                // inside the books remember we already defined books as an arraylist containing Book objects earlier
//                resultString = resultString + book.title + "\n"+
//                        book.publishedDate + "\n\n";
//                // so now we've created a single sentence containing title and publisherdate from the books retrieved from the api
//            }
            BooksAdapter adapter = new BooksAdapter(books);
            rvBooks.setAdapter(adapter);
        }

        @Override
        protected void onPreExecute() {// The method is called before code is run in the async task class
            mLoadingProgress.setVisibility(View.VISIBLE); // view to the progress bar
        }
    }
}