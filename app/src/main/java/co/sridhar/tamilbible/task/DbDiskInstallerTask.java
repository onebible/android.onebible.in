package co.sridhar.tamilbible.task;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.ProgressBar;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Language;

public class DbDiskInstallerTask extends AsyncTask<String, Boolean, Boolean> {

    private Context mContext;
    private Language mLanguage;
    private Callback mCallback;
    private ProgressBar mProgressBar;

    public DbDiskInstallerTask(Context context, Language language, ProgressBar progressBar, Callback callback) {
        this.mLanguage = language;
        this.mContext = context;
        this.mCallback = callback;
        this.mProgressBar = progressBar;
    }

    @Override
    protected void onPreExecute() {
        mProgressBar.setProgress(0);
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

            int total = 31102;
            int i = 3110;
            while ((line = reader.readLine()) != null) {
                if (isCancelled()) {
                    break;
                }
                //mProgressBar.setProgress(total/i);
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
    }


    public interface Callback {
        void onDone();
    }
}

