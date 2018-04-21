package co.sridhar.tamilbible.service;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Language;

public class LanguageService {

    private static String TABLE_NAME = "tbl_languages";

    private DatabaseHelper dbh;

    public LanguageService(Context context) {
        dbh = new DatabaseHelper(context);
    }

    public ArrayList<Language> getInstalledLanguages() {
        Cursor cursor = null;
        ArrayList<Language> languages = new ArrayList<>();

        try {
            SQLiteDatabase sqlDb = dbh.getReadableDatabase();
            cursor = sqlDb.rawQuery("SELECT * FROM " + TABLE_NAME + " where is_installed = 1 ", null);

            while (cursor.moveToNext()) {
                languages.add(new Language(cursor));
            }
            sqlDb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return languages;
    }

    public void updateInstallation(Language language) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();
            sqlDb.update(TABLE_NAME, language.setInstallationValues(), "suffix=?", new String[]{
                    language.getSuffix()
            });

            sqlDb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void updateRemoval(Language language) {
        try {
            SQLiteDatabase sqlDb = dbh.getWritableDatabase();
            sqlDb.update(TABLE_NAME, language.setRemovalValues(), "suffix=?", new String[]{
                    language.getSuffix()
            });

            sqlDb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ArrayList<Language> getAvailableLanguages() {
        Cursor cursor = null;
        ArrayList<Language> languages = new ArrayList<>();

        try {
            SQLiteDatabase sqlDb = dbh.getReadableDatabase();
            cursor = sqlDb.rawQuery("SELECT * FROM " + TABLE_NAME + " where is_installed = 0 ", null);

            while (cursor.moveToNext()) {
                languages.add(new Language(cursor));
            }
            sqlDb.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return languages;
    }

}
