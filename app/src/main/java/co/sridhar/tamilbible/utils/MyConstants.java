package co.sridhar.tamilbible.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import co.sridhar.tamilbible.settings.AppPreferences;

public final class MyConstants {

    private static MyConstants myConstants;
    private static Map<Integer, String> bookNameToNumber = new HashMap<>();

    private List<String> newTestament = new ArrayList<>();
    private List<String> oldTestament = new ArrayList<>();
    private List<Integer> chapCounts = new ArrayList<>();
    private static List<String> bookNames = new ArrayList<>();

    private String oldTestamentTitle;
    private String newTestamentTitle;
    private String header = "Books";
    private JSONObject rawJSON;

    private MyConstants() {

    }

    public static MyConstants getInstance(Context context) {
        if (myConstants == null) {
            myConstants = new MyConstants();
            myConstants.loadJSONFromAsset(context);
            myConstants.loadBookNameToNumber();
            myConstants.loadTitles();
            myConstants.loadHeader();
            return myConstants;
        } else {
            return myConstants;
        }
    }

    public void refresh(Context context) {
        newTestament.clear();
        oldTestament.clear();
        chapCounts.clear();
        bookNames.clear();
        myConstants.loadJSONFromAsset(context);
        myConstants.loadBookNameToNumber();
        myConstants.loadTitles();
        myConstants.loadHeader();
    }

    private void loadHeader() {
        try {
            header = rawJSON.getJSONObject("header").getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getHeader() {
        return this.header;
    }

    private void loadTitles() {
        try {
            JSONArray books = rawJSON.getJSONArray("sections");
            for (int i = 0; i < books.length(); i++) {
                JSONObject val = books.getJSONObject(i);
                if (val.getBoolean("is_old_testament")) {
                    oldTestamentTitle = val.getString("name");
                } else {
                    newTestamentTitle = val.getString("name");
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getOldTestamentTitle() {
        return oldTestamentTitle;
    }

    public String getNewTestamentTitle() {
        return newTestamentTitle;
    }

    private void loadBookNameToNumber() {
        try {
            JSONArray books = rawJSON.getJSONArray("books");
            for (int i = 0; i < books.length(); i++) {
                JSONObject book = books.getJSONObject(i);
                if (book.getBoolean("is_old_testament")) {
                    oldTestament.add(book.getString("name"));
                } else {
                    newTestament.add(book.getString("name"));
                }
                bookNameToNumber.put(bookNameToNumber.size(), book.getString("name"));
                chapCounts.add(book.getInt("count"));
                bookNames.add(book.getString("name"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static String getBookNameById(String id) {
        Integer bookId = Integer.parseInt(id);
        return bookNames.get(bookId);
    }


    public List<String> getNewTestamentBooks() {
        return newTestament;
    }

    public List<String> getOldTestamentBooks() {
        return oldTestament;
    }

    public Integer getBookNumberToChapterCounts(String bookId) {
        int id = Integer.parseInt(bookId);
        return chapCounts.get(id);
    }

    private String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("json/" + AppPreferences.getInstance().getDefaultLanguage(context) + ".json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);

            is.close();
            json = new String(buffer, "UTF-8");

            rawJSON = new JSONObject(json);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return json;
    }

}

