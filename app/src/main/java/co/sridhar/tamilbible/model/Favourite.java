package co.sridhar.tamilbible.model;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.Html;

public class Favourite {

    private Integer id;

    private String passage;
    private String location;

    private String bookId;
    private String chapterId;
    private String verseId;

    private String ID_COLUMN_NAME = "id";
    private String PASSAGE_COLUMN_NAME = "passage";
    private String LOCATION_COLUMN_NAME = "location";
    private String BOOK_ID_COLUMN_NAME = "book_id";
    private String CHAPTER_ID_COLUMN_NAME = "chapter_id";
    private String VERSE_ID_COLUMN_NAME = "verse_id";

    public void setVerseDetails(Verse verse) {
        this.bookId = verse.getBookId();
        this.chapterId = verse.getChapterId();
        this.verseId = verse.getVerseId();
    }

    public ContentValues serializeAddToDb() {
        ContentValues values = new ContentValues();
        values.put(PASSAGE_COLUMN_NAME, this.passage);
        values.put(LOCATION_COLUMN_NAME, this.location);
        values.put(BOOK_ID_COLUMN_NAME, this.bookId);
        values.put(CHAPTER_ID_COLUMN_NAME, this.chapterId);
        values.put(VERSE_ID_COLUMN_NAME, this.verseId);

        return values;
    }

    public Favourite(Cursor cursor) {
        if (cursor == null) {
            return;
        }
        String idIns = cursor.getString(cursor.getColumnIndex(ID_COLUMN_NAME));
        this.id = Integer.parseInt(idIns);
        this.passage = cursor.getString(cursor.getColumnIndex(PASSAGE_COLUMN_NAME));
        this.location = cursor.getString(cursor.getColumnIndex(LOCATION_COLUMN_NAME));

        this.bookId = cursor.getString(cursor.getColumnIndex(BOOK_ID_COLUMN_NAME));
        this.chapterId = cursor.getString(cursor.getColumnIndex(CHAPTER_ID_COLUMN_NAME));
        this.verseId = cursor.getString(cursor.getColumnIndex(VERSE_ID_COLUMN_NAME));

        String verseNumbers = location.split(":")[1];
        String[] multiVerse = verseNumbers.split(",");
        String[] versesInPassage = passage.split("\r\n");

        String f = "&nbsp;&nbsp;&nbsp;&nbsp;";
        for (int i = 0; i < versesInPassage.length; i++) {
            f = f + "<sup>" + multiVerse[i] + "</sup>" + "  " + versesInPassage[i];
        }
        this.passage = f;
        this.setVerseDetails(new Verse(bookId, chapterId, verseId));
    }

    public Favourite(String passage, String location) {
        this.passage = passage;
        this.location = location;
    }

    public String getPassageForShare() {
        String html;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            html = Html.fromHtml(this.passage, Html.FROM_HTML_MODE_LEGACY).toString();
        } else {
            html = Html.fromHtml(this.passage).toString();
        }
        return html + " \r\n " + this.getLocation();
    }

    public Verse getVerse() {
        return new Verse(this.bookId, this.chapterId, this.verseId);
    }

    public String toString() {
        return this.passage + "\r\n" + this.location;
    }

    public String getPassage() {
        return passage;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

}
