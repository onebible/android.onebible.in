package co.sridhar.tamilbible.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import co.sridhar.tamilbible.settings.AppPreferences;

/**
 * Model for `tbl_language`
 * Created by sridharrajs on 9/25/17.
 */

public class Language {

    private int id;
    private String title;
    private String suffix;

    /**
     * psuedo variables
     **/
    private boolean isInstalled;
    private String downloadUrl;
    private String zipFileName;
    private String sqlFileName;
    private String tableName;

    private String ID_COLUMN_NAME = "id";
    private String TITLE_COLUMN_NAME = "title";
    private String IS_INSTALLED_COLUMN_NAME = "is_installed";
    private String DOWNLOAD_URL_COLUMN_NAME = "download_url";
    private String SUFFIX_COLUMN_NAME = "suffix";

    public Language(String title) {
        this.title = title;
        this.suffix = title.toLowerCase();
        this.sqlFileName = "tbl_bible_" + this.suffix + ".sql";
        this.tableName = "tbl_bible_" + this.suffix;
    }

    public Language(Cursor cursor) {
        if (cursor == null) {
            return;
        }

        String idIns = cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME));
        this.id = Integer.parseInt(idIns);

        this.suffix = cursor.getString(cursor.getColumnIndex(SUFFIX_COLUMN_NAME));
        this.title = cursor.getString(cursor.getColumnIndex(TITLE_COLUMN_NAME));
        this.isInstalled = cursor.getString(cursor.getColumnIndex(IS_INSTALLED_COLUMN_NAME)).equalsIgnoreCase("1");
        this.downloadUrl = cursor.getString(cursor.getColumnIndex(DOWNLOAD_URL_COLUMN_NAME));

        this.zipFileName = "tbl_bible_" + this.suffix + ".sql.zip";
        this.sqlFileName = "tbl_bible_" + this.suffix + ".sql";
        this.tableName = "tbl_bible_" + this.suffix;
    }

    public boolean isProcessingLang(Context context) {
        return this.suffix.equalsIgnoreCase(AppPreferences.getInstance().getInstallLang(context));
    }

    public ContentValues setInstallationValues() {
        ContentValues values = new ContentValues();
        values.put(IS_INSTALLED_COLUMN_NAME, 1);
        return values;
    }

    public ContentValues setRemovalValues() {
        ContentValues values = new ContentValues();
        values.put(IS_INSTALLED_COLUMN_NAME, 0);
        return values;
    }

    public String getDiskDbInstallerSqlFileName() {
        return "langs/" + sqlFileName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSuffix() {
        return suffix;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public String getZipFileName() {
        return zipFileName;
    }

    public String getSqlFileName() {
        return sqlFileName;
    }

    public String getTableName() {
        return tableName;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

}
