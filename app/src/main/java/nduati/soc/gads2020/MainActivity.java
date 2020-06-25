package nduati.soc.gads2020;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private TextView textView, tvError;
    private ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // finds the TextView Widget in activity_main layout file under id tvResponse and binds it to the variable
        textView = findViewById(R.id.tvResponse);
        mLoadingProgress = findViewById(R.id.pb_loading);
        tvError = findViewById(R.id.tv_error);
        try { // remember our function simply throws the error
            URL bookURL = ApiUtil.BuildUrl("cooking"); // calling the builder method we created in our APIUtil class which would append the string to the base url creating a complete url
            new BooksQueryTask().execute(bookURL);
        } catch (Exception e) {
            e.printStackTrace();
        }

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
                textView.setVisibility(View.INVISIBLE); // hide result textView
            }else{ // api call result is not empty so success
                tvError.setVisibility(View.INVISIBLE); // hide error textView
                textView.setVisibility(View.VISIBLE); // show textview
            }
            textView.setText(result);
        }

        @Override
        protected void onPreExecute() {// The method is called before code is run in the async task class
            mLoadingProgress.setVisibility(View.VISIBLE); // view to the progress bar
        }
    }
}