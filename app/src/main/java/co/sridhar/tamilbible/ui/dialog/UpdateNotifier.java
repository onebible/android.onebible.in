package co.sridhar.tamilbible.ui.dialog;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

/**
 * Dialog for Update notifier
 * Created by sridharrajs on 9/26/17.
 */

public class UpdateNotifier {

    public static AlertDialog.Builder buildDialog(final Context mContext, final String packageName) {
        AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Update");
        alert.setMessage("A new version is available. Please update the app for better experience and new features");

        alert.setPositiveButton("Update now", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(mContext, "Opening play store", Toast.LENGTH_SHORT).show();
                Uri uri = Uri.parse("market://details?id=" + packageName);
                Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                try {
                    mContext.startActivity(myAppLinkToMarket);
                } catch (ActivityNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        alert.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        return alert;
    }

}
