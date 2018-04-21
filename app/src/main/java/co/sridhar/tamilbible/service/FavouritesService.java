package co.sridhar.tamilbible.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Favourite;
import co.sridhar.tamilbible.model.Verse;

public class FavouritesService {

    private static String TABLE_NAME = "tbl_favourites";

    public static List<Favourite> getAllFavourites(DatabaseHelper dbh) {
        Cursor cursor = null;
        List<Favourite> verses = new ArrayList<>();

        try {
            SQLiteDatabase sqlDb = dbh.getReadableDatabase();
            cursor = sqlDb.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY time_stamp desc", null);

            while (cursor.moveToNext()) {
                verses.add(new Favourite(cursor));
            }
            sqlDb.close();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return verses;
    }

    public static boolean add(DatabaseHelper dbh, List<Favourite> favourites) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();

            for (Favourite favourite : favourites) {
                sqlDb.insert(TABLE_NAME, null, favourite.serializeAddToDb());
            }

            sqlDb.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean add(DatabaseHelper dbh, Favourite favourite) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();
            sqlDb.insert(TABLE_NAME, null, favourite.serializeAddToDb());
            sqlDb.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean remove(DatabaseHelper dbh, int position) {
        try {
            SQLiteDatabase ssdb = dbh.getWritableDatabase();
            ssdb.execSQL("delete from " + TABLE_NAME + " where id='" + position + "'");
            ssdb.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean remove(DatabaseHelper dbh, Verse verse) {
        try {
            SQLiteDatabase ssdb = dbh.getWritableDatabase();
            ssdb.execSQL("delete from " + TABLE_NAME + " where book_id=? and chapter_id=? and verse_id=?", new String[]{
                    verse.getBookId(),
                    verse.getChapterId(),
                    verse.getVerseId()
            });
            ssdb.close();
            return true;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

}
