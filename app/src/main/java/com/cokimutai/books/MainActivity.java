package com.cokimutai.books;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            URL bookUrl = ApiUtils.buildUrl("cooking");
            new BooksQueryTask().execute(bookUrl);

        }
        catch (Exception e) {
           // e.printStackTrace();
            Log.d("Error", e.getMessage());

        }
    }

    public class BooksQueryTask extends AsyncTask<URL, Void, String>{

        @Override
        protected String doInBackground(URL... urls) {
            URL searchUrl = urls[0];
            String results = null;
            try{
                results = ApiUtils.getJson(searchUrl);
            }
            catch (IOException e){
                Log.e("Error ", e.getMessage());

            }
            return results;

        }

        @Override
        protected void onPostExecute(String result) {
            TextView tvResults = (TextView)findViewById(R.id.tvResponse);
            tvResults.setText(result);

        }
    }
}


