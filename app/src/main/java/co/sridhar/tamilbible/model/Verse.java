package co.sridhar.tamilbible.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.graphics.Color;

import co.sridhar.tamilbible.utils.MyConstants;

@SuppressWarnings("FieldCanBeLocal")
public class Verse {

    private int id;

    private String verseId;
    private String verse;

    private String chapterId;
    private String chapterName;

    private String bookId;

    private String favourited;

    private int color = Color.rgb(224, 224, 224);

    private String VERSE_ID_COLUMN_NAME = "verse_id";
    private String VERSE_COLUMN_NAME = "verse";
    private String IS_FAVOURITE_COLUMN_NAME = "is_favourited";
    private String CHAPTER_ID_COLUMN_NAME = "chapter_id";
    private String BOOK_ID_COLUMN_NAME = "book_id";

    Verse(String bookId, String chapterId, String verseId) {
        this.bookId = bookId;
        this.chapterId = chapterId;
        this.verseId = verseId;
    }

    public Verse(Cursor cursor) {
        if (cursor == null) {
            return;
        }

        this.verseId = cursor.getString(cursor.getColumnIndex(VERSE_ID_COLUMN_NAME));
        this.verse = cursor.getString(cursor.getColumnIndex(VERSE_COLUMN_NAME));
        this.favourited = cursor.getString(cursor.getColumnIndex(IS_FAVOURITE_COLUMN_NAME));

        this.bookId = cursor.getString(cursor.getColumnIndex(BOOK_ID_COLUMN_NAME));
        this.chapterId = cursor.getString(cursor.getColumnIndex(CHAPTER_ID_COLUMN_NAME));

        this.chapterName = MyConstants.getBookNameById(bookId);
    }

    public ContentValues setFavouriteValues() {
        ContentValues values = new ContentValues();
        values.put(IS_FAVOURITE_COLUMN_NAME, 1);
        return values;
    }

    public ContentValues unFavouriteValues() {
        ContentValues values = new ContentValues();
        values.put(IS_FAVOURITE_COLUMN_NAME, 0);
        return values;
    }

    public String getLocation() {
        return chapterName + " " + chapterId + " : " + verseId;
    }

    @Override
    public String toString() {
        return "this.verseId " + this.verseId +
                "this.verse" + this.verse;
    }

    public boolean isFavourited() {
        return Integer.parseInt(this.favourited) == 1;
    }

    public void setFavourited(String favourited) {
        this.favourited = favourited;
    }

    public String getChapterName() {
        return chapterName;
    }

    public String getVerse() {
        return verse;
    }

    public String getVerseId() {
        return verseId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public void setVerse(String verse) {
        this.verse = verse;
    }

    public String getBookId() {
        return bookId;
    }

}
