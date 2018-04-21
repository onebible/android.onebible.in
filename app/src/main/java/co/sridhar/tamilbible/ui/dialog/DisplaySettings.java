package co.sridhar.tamilbible.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.settings.AppPreferences;

/**
 * Display settings dialog
 * Created by sridharrajs on 9/26/17.
 */

public class DisplaySettings {

    public static AlertDialog.Builder buildDialog(final Context context, final Callback callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setTitle("Display Settings");

        LayoutInflater inflater = LayoutInflater.from(context);

        View dialogView = inflater.inflate(R.layout.display_settings, null);
        final CheckBox isSecondLang = (CheckBox) dialogView.findViewById(R.id.show_second_lang_chkbox);

        if (AppPreferences.getInstance().isShowSecondLanguage(context)) {
            isSecondLang.setChecked(true);
        } else {
            isSecondLang.setChecked(false);
        }

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (isSecondLang.isChecked()) {
                    AppPreferences.getInstance().enableSecondLanguage(context);
                } else {
                    AppPreferences.getInstance().disableSecondLanguage(context);
                }

                callback.onBeforeDismiss();
                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Close", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                callback.onBeforeDismiss();
                dialog.dismiss();
            }
        });

        alert.setView(dialogView);

        return alert;
    }

    public interface Callback {
        void onBeforeDismiss();
    }

}
