package co.sridhar.tamilbible.activity;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.adapter.DbInstallerAdapter;
import co.sridhar.tamilbible.fragments.AvailableLanguageFragment;
import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Language;
import co.sridhar.tamilbible.service.LanguageService;
import co.sridhar.tamilbible.service.VerseService;
import co.sridhar.tamilbible.settings.AppPreferences;

public class DbInstallerActivity extends AppCompatActivity {

    private ProgressBar mProgressBar;
    private LinearLayout mProgressLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_db_installer);

        setTitle("Choose the Bible language");

        mLanguageService = new LanguageService(getBaseContext());

        ArrayList<Language> languages = new ArrayList<>();

        for (String title : new String[]{
                "Hindi",
                "Tamil",
                "Bengali",
                "Gujarati",
                "Kannada",
                "Malayalam",
                "Oriya",
                "Punjabi",
                "English",
                "Telugu",
        }) {
            languages.add(new Language(title));
        }

        ListView newView = (ListView) findViewById(R.id.new_id);
        DbInstallerAdapter adapter = new DbInstallerAdapter(getBaseContext(), languages, new DbInstallerAdapter.Listener() {
            @Override
            public void onClick(Language language) {
                mInstallationLanguage = language;
            }
        });

        newView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        newView.setAdapter(adapter);

        mProgressBar = (ProgressBar) findViewById(R.id.hoz_progress_bar);
        mProgressLayout = (LinearLayout) findViewById(R.id.progress_layout);
    }

    private DbDiskInstallerTask installationTask;
    private LanguageService mLanguageService;
    private Language mInstallationLanguage;

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem proceed = menu.findItem(R.id.proceed);
        proceed.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                return executeViaTask(item);
            }
        });
        return super.onPrepareOptionsMenu(menu);
    }


    private boolean executeViaAsync() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {

                try {
                    String fileName = mInstallationLanguage.getDiskDbInstallerSqlFileName();
                    DatabaseHelper dbHelper = new DatabaseHelper(getBaseContext());
                    InputStream in = getBaseContext().getAssets().open(fileName);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("info => mProgressBar.()", mProgressBar.getProgress() + "");
                                int progress = mProgressBar.getProgress() + 1;
                                Log.i("info => progress", progress + "");
                                mProgressBar.setProgress(progress);
                            }
                        });

                        System.out.println("#############" + line);
                        dbHelper.getWritableDatabase().execSQL(line);
                    }
                    System.out.println("Script file executed was " + fileName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        };
        AsyncTask.execute(runnable);
        return false;
    }

    private boolean executeViaTask(MenuItem item) {
        item.setEnabled(false);

        if (mInstallationLanguage == null) {
            Toast.makeText(getBaseContext(), "Please select a language to install", Toast.LENGTH_SHORT).show();
            item.setEnabled(true);
            return false;
        }

        if (mInstallationLanguage.getSuffix().equalsIgnoreCase("tamil")) {
            startActivity(new Intent(DbInstallerActivity.this, MainActivity.class));
            DbInstallerActivity.this.finish();
            finish();
            return false;
        }

        Toast.makeText(getBaseContext(), "Preparing to add " + mInstallationLanguage.getTitle(), Toast.LENGTH_SHORT).show();
        AppPreferences.getInstance().setStartDbInstallation(getBaseContext());

        installationTask = new DbDiskInstallerTask(getBaseContext(), mInstallationLanguage, new AvailableLanguageFragment.Callback() {
            @Override
            public void onDone() {
                Toast.makeText(getBaseContext(), "Opening the app", Toast.LENGTH_LONG).show();
                mLanguageService.updateInstallation(mInstallationLanguage);
                mProgressLayout.setVisibility(View.INVISIBLE);
                AppPreferences.getInstance().setCompleteDbInstallation(getBaseContext());
                AppPreferences.getInstance().saveDefaultLanguage(getBaseContext(), mInstallationLanguage.getSuffix());

                if (!mInstallationLanguage.getSuffix().equalsIgnoreCase("tamil")) {
                    Language removeLang = new Language("Tamil");
                    LanguageService mLanguageService = new LanguageService(getBaseContext());
                    mLanguageService.updateRemoval(removeLang);
                    VerseService verseService = new VerseService(getBaseContext());
                    verseService.dropTable(removeLang);
                }

                startActivity(new Intent(DbInstallerActivity.this, MainActivity.class));
                DbInstallerActivity.this.finish();
                finish();
            }
        });
        installationTask.execute();
        mProgressLayout.setVisibility(View.VISIBLE);
        Toast.makeText(getBaseContext(), "Please DON'T CLOSE THE APP or MOVE AWAY FROM THIS SCREEEN. It will normally take 2 minutes for it complete.", Toast.LENGTH_LONG).show();
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.db_installer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    class DbDiskInstallerTask extends AsyncTask<String, Boolean, Boolean> {

        private Context mContext;
        private Language mLanguage;
        private AvailableLanguageFragment.Callback mCallback;

        DbDiskInstallerTask(Context context, Language language, AvailableLanguageFragment.Callback callback) {
            this.mLanguage = language;
            this.mContext = context;
            this.mCallback = callback;
        }

        @Override
        protected void onPreExecute() {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.setProgress(1);
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {
            try {
                String fileName = mLanguage.getDiskDbInstallerSqlFileName();
                DatabaseHelper dbHelper = new DatabaseHelper(mContext);
                InputStream in = mContext.getAssets().open(fileName);
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;

                while ((line = reader.readLine()) != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.i("info => mProgressBar.()", mProgressBar.getProgress() + "");
                            int progress = mProgressBar.getProgress() + 1;
                            Log.i("info => progress", progress + "");
                            mProgressBar.setProgress(progress);
                        }
                    });

                    System.out.println("#############" + line);
                    dbHelper.getWritableDatabase().execSQL(line);
                }
                System.out.println("Script file executed was " + fileName);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Error when creating database in runDbOnAppScript()::DatabaseHelper");
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            mCallback.onDone();
            mProgressBar.setProgress(100);
        }

    }


}
