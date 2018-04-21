package co.sridhar.tamilbible.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.File;

import co.sridhar.tamilbible.model.Language;

public class FileTask extends AsyncTask<String, Boolean, Boolean> {

    private Language mLanguage;

    public FileTask(Language language) {
        this.mLanguage = language;
    }

    @Override
    protected Boolean doInBackground(String... urls) {
        try {
            File zipFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), mLanguage.getZipFileName());
            zipFile.delete();
            File sqlFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), mLanguage.getSqlFileName());
            sqlFile.delete();
            return true;
        } catch (Exception e) {
            Log.e("error => e", e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

}

