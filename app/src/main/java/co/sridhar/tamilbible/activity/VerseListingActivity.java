package co.sridhar.tamilbible.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.fragments.VerseListingFragment;
import co.sridhar.tamilbible.utils.MyConstants;

public class VerseListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verse_listing_activity);

        String chapterId = getIntent().getExtras().getString("chapter_id");
        String bookId = getIntent().getExtras().getString("book_id");
        String searchVerseId = getIntent().getExtras().getString("search_verse_id");

        String title = MyConstants.getInstance(getBaseContext()).getBookNameById(bookId) + " " + chapterId;
        setTitle(title);

        Fragment verseListingFragment = new VerseListingFragment();

        Bundle args = new Bundle();
        args.putString("book_id", bookId);
        args.putString("chapter_id", chapterId);
        args.putString("search_verse_id", searchVerseId);
        verseListingFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.verse_listing_child, verseListingFragment).commit();

    }
}
