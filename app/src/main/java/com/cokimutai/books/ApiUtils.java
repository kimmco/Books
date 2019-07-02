package com.cokimutai.books;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Scanner;

public class ApiUtils {

    private ApiUtils() {
    }

    public static final String BASE_API_URL =
            "https://www.googleapis.com/books/v1/volumes";


    public static URL buildUrl(String title){
        String fullUrl = BASE_API_URL + "?q=" + title;
        URL url = null;
        try {
            url = new URL(fullUrl);

        }catch (Exception e){
            e.printStackTrace();
        }
        return url;
    }

    public static String getJson(URL url) throws IOException {

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();


        try {
            InputStream stream  = connection.getInputStream();
            Scanner scanner = new Scanner(stream);
            scanner.useDelimiter("\\A");
                boolean hasData = scanner.hasNext();
                if (hasData) {
                    return scanner.next();

                } else {
                    return null;
                }

        }
        catch (Exception e){
            Log.e("Error", e.toString());
            return null;
        }
        finally {
            connection.disconnect();
        }
    }

    public static ArrayList<Book> getBooksFromJson(String json){
        final String ID = "id";
        final String TITLE =  "title";
        final String SUBTITLE =  "subtitle";
        final String AUTHORS=  "authors";
        final String PUBLISHER =  "publisher";
        final String PUBLISHEDATE =  "publishedDate";
        final String VOLUMEINFO = "volumeInfo";
        final String ITEMS =  "items";

        ArrayList<Book> books = new ArrayList<Book>();

        try {
            JSONObject jsonBooks = new JSONObject(json);
            JSONArray arrayBooks = jsonBooks.getJSONArray(ITEMS);
            int numberOfBooks = arrayBooks.length();

            for (int i = 0; i < numberOfBooks; i++){
                JSONObject bookJSON  = arrayBooks.getJSONObject(i);
                JSONObject volumeInfoJSON = bookJSON.getJSONObject(VOLUMEINFO);
                int authorNum = volumeInfoJSON.getJSONArray(AUTHORS).length();
                String[] authors = new String[authorNum];
                for (int j = 0; j < authorNum; j++){
                    authors[j] = volumeInfoJSON.getJSONArray(AUTHORS).get(j).toString();

                }
                Book book = new Book(
                        bookJSON.getString(ID),
                        volumeInfoJSON.getString(TITLE),
                        (volumeInfoJSON.isNull(SUBTITLE)?"":volumeInfoJSON.getString(SUBTITLE)),
                        authors,
                        volumeInfoJSON.getString(PUBLISHER),
                        volumeInfoJSON.getString(PUBLISHEDATE));
                books.add(book);

            }

        }
        catch (JSONException e){
            e.printStackTrace();
        }
        return books;
    }

}

