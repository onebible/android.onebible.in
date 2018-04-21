package co.sridhar.tamilbible.task;

import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import co.sridhar.tamilbible.model.Language;

public class FileSystemTask extends AsyncTask<Void, Void, Void> {

    private Language mLanguage;

    public FileSystemTask(Language language) {
        this.mLanguage = language;
    }

    @Override
    protected Void doInBackground(Void... Void) {
        try {
            unzip();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void unzip() {
        try {
            String destination = Environment.getExternalStorageDirectory().getAbsolutePath();
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), mLanguage.getZipFileName());

            FileInputStream inputStream = new FileInputStream(file);
            ZipInputStream zipStream = new ZipInputStream(inputStream);
            ZipEntry zEntry = null;
            while ((zEntry = zipStream.getNextEntry()) != null) {
                Log.d("Unzip", "Unzipping " + zEntry.getName() + " at " + destination);

                FileOutputStream fout = new FileOutputStream(destination + "/" + zEntry.getName());
                BufferedOutputStream bufout = new BufferedOutputStream(fout);
                byte[] buffer = new byte[1024];
                int read = 0;
                while ((read = zipStream.read(buffer)) != -1) {
                    bufout.write(buffer, 0, read);
                }

                zipStream.closeEntry();
                bufout.close();
                fout.close();

            }
            zipStream.close();
            Log.d("Unzip", "Unzipping complete. path :  " + destination);
        } catch (Exception e) {
            Log.d("Unzip", "Unzipping failed");
            e.printStackTrace();
        }

    }

}

