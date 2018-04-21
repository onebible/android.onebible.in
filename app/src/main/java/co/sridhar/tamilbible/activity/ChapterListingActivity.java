package co.sridhar.tamilbible.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.fragments.ChapterListingFragment;

public class ChapterListingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_listing);

        ChapterListingFragment chapterListingFragment = new ChapterListingFragment();

        Bundle args = new Bundle();
        args.putString("book_id", getIntent().getExtras().getString("book_id"));
        chapterListingFragment.setArguments(args);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.chapter_frag, chapterListingFragment);
        transaction.commit();

    }
}
