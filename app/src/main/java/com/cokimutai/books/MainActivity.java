package com.cokimutai.books;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ProgressBar pdLoading;
    private TextView errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        errorMessage = findViewById(R.id.tv_error);
        pdLoading = findViewById(R.id.pb_loading);

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
            TextView tvResults = findViewById(R.id.tvResponse);


            tvResults.setText(result);
            if (result == null){
                errorMessage.setVisibility(View.VISIBLE);
                pdLoading.setVisibility(View.INVISIBLE);
            }
               pdLoading.setVisibility(View.INVISIBLE);
            ArrayList<Book> books = ApiUtils.getBooksFromJson(result);
            String resultString = "";
            for (Book book : books){
                resultString = resultString + book.title + "\n" +
                        book.publishedDate + "\n\n";

            }
            tvResults.setText(resultString);

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setVisibility(View.VISIBLE);

        }
    }
}


