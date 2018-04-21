package co.sridhar.tamilbible.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import co.sridhar.tamilbible.R;
import co.sridhar.tamilbible.settings.AppPreferences;
import co.sridhar.tamilbible.settings.Defaults;

/**
 * Font settings dialog
 * Created by sridharrajs on 9/26/17.
 */

public class FontSettings {

    public static AlertDialog.Builder buildDialog(final Context mContext, final Callback callback) {
        final AlertDialog.Builder alert = new AlertDialog.Builder(mContext);
        alert.setTitle("Font Settings");

        LayoutInflater inflater = LayoutInflater.from(mContext);

        View dialogView = inflater.inflate(R.layout.font_settings, null);

        final CheckBox isMakeBold = (CheckBox) dialogView.findViewById(R.id.bold_verse_chkbox);
        final CheckBox isLessSpacing = (CheckBox) dialogView.findViewById(R.id.compact_chkbox);

        if (AppPreferences.getInstance().isLightMode(mContext)) {
            isMakeBold.setEnabled(true);
        } else {
            isMakeBold.setEnabled(false);
        }

        if (AppPreferences.getInstance().isLessSpaceView(mContext)) {
            isLessSpacing.setChecked(true);
        } else {
            isLessSpacing.setChecked(false);
        }

        final int min = Defaults.getInstance().getSliderMinValue();
        float fontSize = AppPreferences.getInstance().getFontSize(mContext);

        int textColor = AppPreferences.getInstance().getTextColor(mContext);

        if (textColor == R.color.black) {
            isMakeBold.setChecked(true);
        }

        final TextView currentFontSizeTxtView = (TextView) dialogView.findViewById(R.id.current_tv);
        currentFontSizeTxtView.setText("Your font size is " + Math.round(fontSize));

        final SeekBar simpleSeekBar = (SeekBar) dialogView.findViewById(R.id.seek_bar);
        simpleSeekBar.setProgress(Math.round(fontSize - min));
        simpleSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChangedValue = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChangedValue = min + progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                currentFontSizeTxtView.setText("Your font size will be " + progressChangedValue);
            }
        });

        alert.setView(dialogView);

        alert.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                int fontSize = simpleSeekBar.getProgress() + min;
                AppPreferences.getInstance().saveFontSize(mContext, fontSize);

                if (isMakeBold.isChecked()) {
                    AppPreferences.getInstance().saveFontColor(mContext, R.color.black);
                } else {
                    AppPreferences.getInstance().resetToDefaultTextColor(mContext);
                }

                if (isLessSpacing.isChecked()) {
                    AppPreferences.getInstance().enableCompactView(mContext);
                } else {
                    AppPreferences.getInstance().disableCompactView(mContext);
                }

                callback.onBeforeDismiss();

                dialog.dismiss();
            }
        });

        alert.setNegativeButton("Reset to Defaults", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AppPreferences.getInstance().resetToDefaults(mContext);
                callback.onBeforeDismiss();
                dialog.dismiss();
            }
        });

        return alert;
    }

    public interface Callback {
        void onBeforeDismiss();
    }
}
