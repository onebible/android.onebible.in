package co.sridhar.tamilbible.model;

import android.content.ContentValues;
import android.database.Cursor;

import co.sridhar.tamilbible.utils.TimeUtils;

public class Note {

    private Integer id;
    private String title;
    private String description;

    private String createdAt;
    private String updatedAt;

    private String ID_COLUMN_NAME = "id";
    private String TITLE_COLUMN_NAME = "title";
    private String DESCRIPTION_COLUMN_NAME = "description";
    private String CREATED_AT_COLUMN_NAME = "created_at";
    private String UPDATED_AT_COLUMN_NAME = "updated_at";

    public Note(Cursor cursor) {
        if (cursor == null) {
            return;
        }
        String idIns = cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME));
        this.id = Integer.parseInt(idIns);

        this.title = cursor.getString(cursor.getColumnIndex(TITLE_COLUMN_NAME));
        this.description = cursor.getString(cursor.getColumnIndex(DESCRIPTION_COLUMN_NAME));

        this.createdAt = cursor.getString(cursor.getColumnIndex(CREATED_AT_COLUMN_NAME));
        this.updatedAt = cursor.getString(cursor.getColumnIndex(UPDATED_AT_COLUMN_NAME));
    }

    public ContentValues serializeAddToDb() {

        ContentValues values = new ContentValues();
        values.put(TITLE_COLUMN_NAME, this.title);
        values.put(DESCRIPTION_COLUMN_NAME, this.description);
        values.put(CREATED_AT_COLUMN_NAME, TimeUtils.getDateTime());
        values.put(UPDATED_AT_COLUMN_NAME, TimeUtils.getDateTime());
        return values;
    }

    public ContentValues serializeEditToDb() {
        ContentValues values = new ContentValues();
        values.put(TITLE_COLUMN_NAME, this.title);
        values.put(DESCRIPTION_COLUMN_NAME, this.description);
        values.put(UPDATED_AT_COLUMN_NAME, TimeUtils.getDateTime());
        return values;
    }

    public Note(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public String toString() {
        return "id = " + this.id + " title = " + this.title + " description = " + this.description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

}
