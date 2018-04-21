package co.sridhar.tamilbible.task;

import android.app.AlertDialog;
import android.content.Context;
import android.os.AsyncTask;

import co.sridhar.tamilbible.ui.dialog.UpdateNotifier;
import co.sridhar.tamilbible.utils.UpdateUtils;

public class GetVersionCode extends AsyncTask<Void, String, Boolean> {

    private Context mContext;
    private String currentVersion;
    private String packageName;

    public GetVersionCode(Context context, String currentVersion, String packageName) {
        this.mContext = context;
        this.currentVersion = currentVersion;
        this.packageName = packageName;
    }

    @Override
    protected Boolean doInBackground(Void... Void) {
        try {
            UpdateUtils.checkNow(mContext, currentVersion, new UpdateUtils.Callback() {
                @Override
                public void onResponse(Boolean isUpdateAvailable) {
                    if (isUpdateAvailable) {
                        AlertDialog.Builder alert = UpdateNotifier.buildDialog(mContext, packageName);
                        alert.show();
                    }
                }

                @Override
                public void onError(Exception ex) {
                    // Silence
                }
            });
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean onlineVersion) {
        super.onPostExecute(onlineVersion);
    }

}