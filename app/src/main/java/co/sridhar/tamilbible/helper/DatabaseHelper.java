package co.sridhar.tamilbible.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static String PROD_DB = "tamil_bible_1_202_.db";

    private String DB_PATH_WITH_DB_NAME;
    private static String DB_NAME = PROD_DB;
    private final Context myContext;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, 11);
        this.myContext = context;
        DB_PATH_WITH_DB_NAME = myContext.getDatabasePath(DatabaseHelper.DB_NAME).getPath();
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transfering bytestream.
     */
    @Override
    public synchronized void close() {
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            runDbScript(db, "db_v1.sql");
            runDbScript(db, "db_v2.sql");
            runDbScript(db, "db_v3.sql");
            runDbScript(db, "db_v4.sql");
            runDbScript(db, "db_v5.sql");
            runDbScript(db, "db_v6.sql");
            runDbScript(db, "db_v7.sql");
            runDbScript(db, "db_v8.sql");
            runDbScript(db, "db_v9.sql");
            runDbScript(db, "db_v10.sql");
            runDbScript(db, "db_v11.sql");
            runDbScript(db, "db_v12.sql");
            runDbScript(db, "db_v13.sql");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error when creating database in onCreate()::DatabaseHelper");
        }
    }

    public void newDb(SQLiteDatabase db, String fileName) {
        try {
            final File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), fileName);
            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                db.execSQL(line);
                Log.d("Debugging => line", line);
            }
            br.close();

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error when creating database in onCreate()::DatabaseHelper");
        }
    }

    private void runDbScript(SQLiteDatabase db, String fileName) {
        try {
            InputStream in = myContext.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                try {
                    System.out.println("#############" + line);
                    db.execSQL(line);
                } catch (Exception ex) {
                    Log.e("error in executing SQL", ex.toString());
                    ex.printStackTrace();
                }
            }
            System.out.println("Script file executed was " + fileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error when creating database in onCreate()::DatabaseHelper");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("oldVersion " + oldVersion);
        switch (oldVersion) {
            case 1:
                runDbScript(db, "db_v2.sql");
            case 2:
                runDbScript(db, "db_v3.sql");
            case 3:
                runDbScript(db, "db_v4.sql");
            case 4:
                runDbScript(db, "db_v5.sql");
            case 5:
                runDbScript(db, "db_v6.sql");
            case 6:
                runDbScript(db, "db_v7.sql");
            case 7:
                runDbScript(db, "db_v8.sql");
            case 8:
                runDbScript(db, "db_v9.sql");
            case 9:
                runDbScript(db, "db_v10.sql");
            case 10:
                runDbScript(db, "db_v11.sql");
            case 11:
                runDbScript(db, "db_v12.sql");
            case 12:
                runDbScript(db, "db_v13.sql");
        }
    }
}
