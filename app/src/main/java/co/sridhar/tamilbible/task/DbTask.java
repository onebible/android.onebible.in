package co.sridhar.tamilbible.task;

import android.content.Context;
import android.os.AsyncTask;

import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Language;

public class DbTask extends AsyncTask<String, Boolean, Boolean> {

    private Context mContext;
    private Language mLanguage;
    private Callback mCallback;

    public DbTask(Context context, Language language, Callback callback) {
        this.mLanguage = language;
        this.mContext = context;
        this.mCallback = callback;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        try {
            DatabaseHelper dbHelper = new DatabaseHelper(mContext);
            dbHelper.newDb(dbHelper.getWritableDatabase(), mLanguage.getSqlFileName());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        new FileTask(mLanguage).execute();
        mCallback.onDone();
    }


    public interface Callback {
        void onDone();
    }
}

