package co.sridhar.tamilbible.fragments;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.adapter.LanguageAdapter;
import co.sridhar.tamilbible.helper.DatabaseHelper;
import co.sridhar.tamilbible.model.Language;
import co.sridhar.tamilbible.service.LanguageService;
import co.sridhar.tamilbible.settings.AppPreferences;
import co.sridhar.tamilbible.task.DbTask;
import co.sridhar.tamilbible.task.DownloadFileFromURL;
import co.sridhar.tamilbible.task.FileSystemTask;
import co.sridhar.tamilbible.task.FileTask;
import co.sridhar.tamilbible.ui.dialog.HaltLanguageInstallation;

public class AvailableLanguageFragment extends Fragment implements LanguageAdapter.Listener {

    private ListView listView;
    private LanguageService mLanguageService;
    private TextView emptyTextView;
    private Language mInstallationLanguage;
    private ProgressBar mProgressBar;
    private LinearLayout mProgressLayout;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.language_view_pager, container, false);
        listView = (ListView) view.findViewById(R.id.lang_list_view);
        emptyTextView = (TextView) view.findViewById(R.id.empty_tv);

        mProgressBar = (ProgressBar) view.findViewById(R.id.hoz_progress_bar);
        mProgressLayout = (LinearLayout) view.findViewById(R.id.progress_layout);
        mLanguageService = new LanguageService(getContext());

        refreshData();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    private void refreshData() {
        if (emptyTextView == null || listView == null) {
            return;
        }

        ArrayList<Language> availableLanguages = mLanguageService.getAvailableLanguages();

        if (availableLanguages.isEmpty()) {
            emptyTextView.setVisibility(View.VISIBLE);
            emptyTextView.setText("You've installed all the available languages");
            listView.setVisibility(View.INVISIBLE);
        } else {
            emptyTextView.setVisibility(View.INVISIBLE);
            listView.setVisibility(View.VISIBLE);
            LanguageAdapter la = new LanguageAdapter(getContext(), availableLanguages, this);
            la.notifyDataSetChanged();
            listView.setAdapter(la);
        }

        if (AppPreferences.getInstance().isInstallLang(getContext())) {
            mProgressLayout.setVisibility(View.INVISIBLE);
        } else {
            mProgressLayout.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onDownloadClick(final Language language) {
        if (AppPreferences.getInstance().isInstallLangInProgress(getContext())) {
            Toast.makeText(getContext(), "Already 1 installation is in progress, try again after it completes ", Toast.LENGTH_LONG).show();
            return;
        }

        AppPreferences.getInstance().setInstallLang(getContext(), language.getSuffix());
        refreshData();

        mInstallationLanguage = language;
        initiateInstallationFromDisk();
    }

    DbDiskInstallerTask installationTask;

    private void initiateInstallationFromDisk() {
        mProgressLayout.setVisibility(View.VISIBLE);
        AppPreferences.getInstance().setStartDbInstallation(getContext());
        Toast.makeText(getContext(), "Preparing for installing", Toast.LENGTH_SHORT).show();

        installationTask = new DbDiskInstallerTask(getContext(), mInstallationLanguage, new Callback() {
            @Override
            public void onDone() {
                if (getContext() != null) {
                    AppPreferences.getInstance().setCompleteDbInstallation(getContext());
                    Toast.makeText(getContext(), "Installed " + mInstallationLanguage.getTitle() + " language", Toast.LENGTH_LONG).show();
                    mLanguageService.updateInstallation(mInstallationLanguage);
                    refreshData();
                }
            }
        });
        installationTask.execute();
        Toast.makeText(getContext(), "Please DON'T CLOSE THE APP or MOVE AWAY FROM THIS SCREEEN. It will normally take 2 minutes for it complete.", Toast.LENGTH_LONG).show();
    }

    private void initiateDbInstallation() {
        AppPreferences.getInstance().setStartDbInstallation(getContext());
        Toast.makeText(getContext(), "Preparing for download", Toast.LENGTH_SHORT).show();
        new FileTask(mInstallationLanguage).execute();
        Toast.makeText(getContext(), "Initiating download", Toast.LENGTH_SHORT).show();
        new DownloadFileFromURL(mInstallationLanguage).execute();
        Toast.makeText(getContext(), "Checking details", Toast.LENGTH_SHORT).show();

        new FileSystemTask(mInstallationLanguage).execute();
        Toast.makeText(getContext(), "Preparing tables", Toast.LENGTH_SHORT).show();

        new DbTask(getContext(), mInstallationLanguage, new DbTask.Callback() {
            @Override
            public void onDone() {
                if (getContext() != null) {
                    AppPreferences.getInstance().setCompleteDbInstallation(getContext());
                    Toast.makeText(getContext(), "Installed " + mInstallationLanguage.getTitle() + " language", Toast.LENGTH_LONG).show();
                    mLanguageService.updateInstallation(mInstallationLanguage);
                    refreshData();
                }
            }
        }).execute();
        Toast.makeText(getContext(), "Don't worry. It will normally take 2 minutes for it complete. We'll notify when it is done", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    initiateDbInstallation();
                    Toast.makeText(getContext(), "Proceeding with installation", Toast.LENGTH_SHORT).show();
                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(getContext(), "Denied", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onDeleteClick(Language language) {

    }

    @Override
    public void onCancelClick(Language language) {
        if (installationTask == null) {
            HaltLanguageInstallation.buildDialog(getContext(), language, new HaltLanguageInstallation.Callback() {
                @Override
                public void onComplete() {
                    AppPreferences.getInstance().clearInstallLang(getContext());
                    refreshData();
                }
            }).show();
        } else {
            installationTask.cancel(true);
            HaltLanguageInstallation.buildDialog(getContext(), language, new HaltLanguageInstallation.Callback() {
                @Override
                public void onComplete() {
                    AppPreferences.getInstance().clearInstallLang(getContext());
                    refreshData();
                }
            }).show();
        }
    }

    class DbDiskInstallerTask extends AsyncTask<String, Boolean, Boolean> {

        private Context mContext;
        private Language mLanguage;
        private Callback mCallback;

        DbDiskInstallerTask(Context context, Language language, Callback callback) {
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
                    if (isCancelled()) {
                        break;
                    }

                    if (getActivity() != null) {
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i("info => mProgressBar.()", mProgressBar.getProgress() + "");
                                int progress = mProgressBar.getProgress() + 1;
                                Log.i("info => progress", progress + "");
                                mProgressBar.setProgress(progress);
                            }
                        });
                    }

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

    public interface Callback {
        void onDone();
    }

}


