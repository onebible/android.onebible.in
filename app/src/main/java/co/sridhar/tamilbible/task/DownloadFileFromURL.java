package co.sridhar.tamilbible.task;

import android.os.AsyncTask;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import co.sridhar.tamilbible.model.Language;

public class DownloadFileFromURL extends AsyncTask<Void, Boolean, Boolean> {

    private Language mLanguage;

    public DownloadFileFromURL(Language language) {
        this.mLanguage = language;
    }

    @Override
    protected Boolean doInBackground(Void... Void) {
        try {
            downloadUrl(mLanguage.getDownloadUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void downloadUrl(String s3Url) throws IOException {
        URL url = new URL(s3Url);
        URLConnection connection = url.openConnection();
        connection.connect();

        InputStream input = new BufferedInputStream(url.openStream(), 8192);
        OutputStream output = new FileOutputStream(Environment.getExternalStorageDirectory().toString() + File.separator + mLanguage.getZipFileName());

        byte data[] = new byte[1024];

        int count;
        while ((count = input.read(data)) != -1) {
            output.write(data, 0, count);
        }

        output.flush();

        output.close();
        input.close();
    }

}

