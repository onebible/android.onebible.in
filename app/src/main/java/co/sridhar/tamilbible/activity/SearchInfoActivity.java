package co.sridhar.tamilbible.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.settings.AppPreferences;

public class SearchInfoActivity extends AppIntro {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("");

        String primaryColor = AppPreferences.getInstance().getPrimaryColor(getBaseContext());

        addSlide(AppIntroFragment.newInstance("To activate Language Keyboard, Go to settings", "", R.drawable.search_image1, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Choose 'Language & Input'", "", R.drawable.search_image2, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Choose 'Gboard'", "", R.drawable.search_image3, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Choose 'Languages'", "", R.drawable.search_image4, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Turn off 'Use system language' and enable 'Active input methods'", "", R.drawable.search_image5, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Turn on your language keyboards eg: Tamil", "", R.drawable.search_image6, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("Click on 'Globe' for Tamil keyboard", "", R.drawable.search_image7, Color.parseColor(primaryColor)));
        addSlide(AppIntroFragment.newInstance("That's it!", "", R.drawable.search_image8, Color.parseColor(primaryColor)));

        setBarColor(Color.parseColor(primaryColor));
        setSeparatorColor(Color.parseColor("#ffffff"));

        showSkipButton(true);
        setProgressButtonEnabled(true);
    }

    @Override
    public void onDonePressed(Fragment currentFragment) {
        super.onDonePressed(currentFragment);
        finish();
    }

    @Override
    public void onSkipPressed(Fragment currentFragment) {
        super.onSkipPressed(currentFragment);
        finish();
    }
}

