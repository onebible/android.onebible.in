package co.sridhar.tamilbible.model;

import android.content.ContentValues;
import android.database.Cursor;

import co.sridhar.tamilbible.utils.TimeUtils;

public class ThreadBookmark {

    private int id;

    private String color;

    private String title;

    private String chapterId;
    private String bookId;

    private String createdAt;
    private String updatedAt;
    private String hexCode;

    private static String ID_COLUMN_NAME = "id";
    private static String TITLE_COLUMN_NAME = "title";
    private static String COLOR_COLUMN_NAME = "color";
    private static String BOOK_ID_COLUMN_NAME = "book_id";
    private static String CHAPTER_ID_COLUMN_NAME = "chapter_id";
    private static String HEX_CODE_COLUMN_NAME = "hex_code";
    private static String UPDATED_AT_COLUMN_NAME = "updated_at";

    public ThreadBookmark(int id, String title, String bookId, String chapterId) {
        this.id = id;
        this.title = title;
        this.chapterId = chapterId;
        this.bookId = bookId;
    }

    public ThreadBookmark(Cursor cursor) {
        if (cursor == null) {
            return;
        }
        String idIns = cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME));
        this.id = Integer.parseInt(idIns);

        this.title = cursor.getString(cursor.getColumnIndex(TITLE_COLUMN_NAME));
        this.color = cursor.getString(cursor.getColumnIndex(COLOR_COLUMN_NAME));

        this.bookId = cursor.getString(cursor.getColumnIndex(BOOK_ID_COLUMN_NAME));
        this.chapterId = cursor.getString(cursor.getColumnIndex(CHAPTER_ID_COLUMN_NAME));
        this.hexCode = cursor.getString(cursor.getColumnIndex(HEX_CODE_COLUMN_NAME));
    }

    public ContentValues serializeToDb() {
        ContentValues values = new ContentValues();
        values.put(TITLE_COLUMN_NAME, this.title);
        values.put(CHAPTER_ID_COLUMN_NAME, this.chapterId);
        values.put(BOOK_ID_COLUMN_NAME, this.bookId);
        values.put(UPDATED_AT_COLUMN_NAME, TimeUtils.getDateTime());
        return values;
    }

    public String getHexCode() {
        return hexCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getChapterId() {
        return chapterId;
    }

    public String getBookId() {
        return bookId;
    }

}
