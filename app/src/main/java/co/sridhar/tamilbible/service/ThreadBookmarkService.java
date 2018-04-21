package co.sridhar.tamilbible.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.ThreadBookmark;

public class ThreadBookmarkService {

    private static String TABLE_NAME = "tbl_thread_bookmarks";

    public static List<ThreadBookmark> getThreads(DatabaseHelper dbh) {
        Cursor cursor = null;
        List<ThreadBookmark> threads = new ArrayList<>();
        try {
            SQLiteDatabase sqlDb = dbh.getReadableDatabase();
            cursor = sqlDb.rawQuery("SELECT * FROM " + TABLE_NAME, null);
            while (cursor.moveToNext()) {
                threads.add(new ThreadBookmark(cursor));
            }
            sqlDb.close();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return threads;
    }

    public static boolean edit(DatabaseHelper dbh, ThreadBookmark thread) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();
            sqlDb.update(TABLE_NAME, thread.serializeToDb(), "id=" + thread.getId(), null);
            sqlDb.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

}
