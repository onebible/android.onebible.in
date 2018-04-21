package co.sridhar.tamilbible.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.service.FavouritesService;
import co.sridhar.tamilbible.settings.AppPreferences;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                final DatabaseHelper dbh = new DatabaseHelper(SplashScreenActivity.this);
                FavouritesService.getAllFavourites(dbh);
            }
        };
        AsyncTask.execute(runnable);

        boolean isFirstStart = AppPreferences.getInstance().isFirstLoad(getBaseContext());
        if (isFirstStart) {
            Intent intent = new Intent(SplashScreenActivity.this, OnBoardingActivity.class);
            SplashScreenActivity.this.finish();
            startActivity(intent);
        } else {
            startActivity(new Intent(SplashScreenActivity.this, MainActivity.class));
            SplashScreenActivity.this.finish();
            finish();
        }

    }
}
