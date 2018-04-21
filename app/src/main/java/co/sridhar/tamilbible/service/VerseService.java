package co.sridhar.tamilbible.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Favourite;
import co.sridhar.tamilbible.model.Language;
import co.sridhar.tamilbible.model.Verse;
import co.sridhar.tamilbible.settings.AppPreferences;

public class VerseService {

    private DatabaseHelper dbh;

    private String tableName;

    public VerseService(Context context) {
        this.dbh = new DatabaseHelper(context);
        this.tableName = "tbl_bible_" + AppPreferences.getInstance().getDefaultLanguage(context);
    }

    public int getRowId(String bookId, String chapterId, String verseId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase ssdb = dbh.getReadableDatabase();
            cursor = ssdb.rawQuery("SELECT rowid, * FROM " + tableName + " where book_id=" + bookId + " and chapter_id=" + chapterId + " and verse_id=" + verseId, null);

            while (cursor.moveToNext()) {
                return Integer.parseInt(cursor.getString(cursor.getColumnIndex("rowid")));
            }
            ssdb.close();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public Verse getByRowId(int rowId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase ssdb = dbh.getReadableDatabase();
            cursor = ssdb.rawQuery("SELECT rowid, * FROM " + tableName + " where rowid=" + rowId, null);
            while (cursor.moveToNext()) {
                return new Verse(cursor);
            }
            ssdb.close();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    public List<Verse> getSearchResultsByPhrase(String phrase) {
        Cursor cursor = null;
        List<Verse> verses = new ArrayList<>();
        try {
            SQLiteDatabase ssdb = dbh.getReadableDatabase();
            cursor = ssdb.rawQuery("SELECT * FROM " + tableName + " where verse like '%" + phrase + "%'", null);

            while (cursor.moveToNext()) {
                verses.add(new Verse(cursor));
            }
            ssdb.close();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return verses;
    }

    public void favourite(DatabaseHelper dbh, List<Verse> verses) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();

            for (Verse verse : verses) {
                sqlDb.update(tableName, verse.setFavouriteValues(), "book_id=? and chapter_id=? and verse_id=?", new String[]{
                        verse.getBookId(),
                        verse.getChapterId(),
                        verse.getVerseId()
                });
            }

            sqlDb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void favourite(Verse verse) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();

            sqlDb.update(tableName, verse.setFavouriteValues(), "book_id=? and chapter_id=? and verse_id=?", new String[]{
                    verse.getBookId(),
                    verse.getChapterId(),
                    verse.getVerseId()
            });

            sqlDb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void unFavourite(Favourite favourite) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();
            Verse verse = favourite.getVerse();

            sqlDb.update(tableName, verse.unFavouriteValues(), "book_id=? and chapter_id=? and verse_id=?", new String[]{
                    verse.getBookId(),
                    verse.getChapterId(),
                    verse.getVerseId()
            });

            sqlDb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Verse> getVerses(String bookId, String chapterId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase ssdb = dbh.getReadableDatabase();
            cursor = ssdb.rawQuery("SELECT * FROM " + tableName + " where book_id=" + bookId + " and chapter_id=" + chapterId, null);
            List<Verse> verses = new ArrayList<>();

            while (cursor.moveToNext()) {
                verses.add(new Verse(cursor));
            }
            ssdb.close();
            return verses;
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return Collections.emptyList();
    }

    public void dropTable(Language language) {
        try {
            SQLiteDatabase ssdb = dbh.getWritableDatabase();
            ssdb.execSQL("drop table if exists " + language.getTableName());
            ssdb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
