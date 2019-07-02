package com.cokimutai.books;

import android.os.AsyncTask;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import android.widget.TextView;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private ProgressBar pdLoading;
    private RecyclerView rvBooks;
    private TextView tvErrorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rvBooks = findViewById(R.id.rv_books);
        pdLoading = findViewById(R.id.pb_loading);
        tvErrorView = findViewById(R.id.tv_error);

        LinearLayoutManager booksLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        rvBooks.setLayoutManager(booksLayoutManager);

        try {
            URL bookUrl = ApiUtils.buildUrl("android");
            new BooksQueryTask().execute(bookUrl);

        }
        catch (Exception e) {
           // e.printStackTrace();
            Log.d("Error", e.getMessage());

        }
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
            URL bookUrl = ApiUtils.buildUrl(query);
            new BooksQueryTask().execute(bookUrl);

        }
        catch (Exception e){

            Log.d("Error", e.getMessage());

        }
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
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
            pdLoading.setVisibility(View.INVISIBLE);
            if (result == null){

                tvErrorView.setVisibility(View.VISIBLE);
                rvBooks.setVisibility(View.INVISIBLE);
            }else {
                tvErrorView.setVisibility(View.INVISIBLE);
                rvBooks.setVisibility(View.VISIBLE);

            }
            ArrayList<Book> books = ApiUtils.getBooksFromJson(result);
            String resultString = "";
            BooksAdapter adapter = new BooksAdapter(books);
            rvBooks.setAdapter(adapter);




        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pdLoading.setVisibility(View.VISIBLE);

        }
    }
}


