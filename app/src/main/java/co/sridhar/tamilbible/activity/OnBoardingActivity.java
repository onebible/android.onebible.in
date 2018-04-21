package co.sridhar.tamilbible.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.widget.Toast;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.settings.AppPreferences;

public class OnBoardingActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");

        String primaryColor = "#02b875";

        addSlide(AppIntroFragment.newInstance("Swipe to switch book", "Choose testaments by a swipe", R.drawable.onboarding_book_selection, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Handy right swipe", "Right swipe to share/favourite verse quickly", R.drawable.onboarding_quick_share, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Multi verse sharing", "You can select multiple verses and share them at one go", R.drawable.onboarding_multi_share, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Powerful search", "Faster, relevant searches for YOU", R.drawable.onboarding_search, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Organise YOUR Meditation", "You can save YOUR meditation as notes", R.drawable.onboarding_notes, Color.parseColor(primaryColor)));

        setBarColor(Color.parseColor(primaryColor));
        setSeparatorColor(Color.parseColor("#ffffff"));

        showSkipButton(false);
        setProgressButtonEnabled(true);
    }


    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);

        AppPreferences.getInstance().saveFirstLoad(getBaseContext());

        //Toast.makeText(this, "Loading your app", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(OnBoardingActivity.this, DbInstallerActivity.class));
        //startActivity(new Intent(OnBoardingActivity.this, MainActivity.class));
        finish();
    }

}
