package co.sridhar.tamilbible.service;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Note;

public class NoteService {

    private static String TABLE_NAME = "tbl_notes";

    public static List<Note> getAllNotes(DatabaseHelper dbh) {
        Cursor cursor = null;
        List<Note> notes = new ArrayList<>();
        try {
            SQLiteDatabase sqlDb = dbh.getReadableDatabase();
            cursor = sqlDb.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY updated_at desc", null);
            while (cursor.moveToNext()) {
                notes.add(new Note(cursor));
            }
            sqlDb.close();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return notes;
    }

    public static boolean remove(DatabaseHelper dbh, int position) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();
            sqlDb.execSQL("delete from " + TABLE_NAME + " where id='" + position + "'");
            sqlDb.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean add(DatabaseHelper dbh, Note note) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();
            sqlDb.insert(TABLE_NAME, null, note.serializeAddToDb());
            sqlDb.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static boolean edit(DatabaseHelper dbh, Note note) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();
            sqlDb.update(TABLE_NAME, note.serializeEditToDb(), "id=" + note.getId(), null);
            sqlDb.close();
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    public static Note getById(DatabaseHelper dbh, String noteId) {
        Cursor cursor = null;
        try {
            SQLiteDatabase sqlDb = dbh.getReadableDatabase();
            cursor = sqlDb.rawQuery("SELECT * FROM " + TABLE_NAME + " where id=" + noteId, null);

            while (cursor.moveToNext()) {
                return new Note(cursor);
            }
            sqlDb.close();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

}
