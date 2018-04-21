package co.sridhar.tamilbible.ui.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

import co.sridhar.tamilbible.model.Language;
import co.sridhar.tamilbible.service.LanguageService;
import co.sridhar.tamilbible.service.VerseService;

/**
 * Confirmation delete language
 * Created by sridharrajs on 9/28/17.
 */

public class HaltLanguageInstallation {

    public static AlertDialog.Builder buildDialog(final Context context, final Language language, final Callback callback) {
        AlertDialog.Builder alert = new AlertDialog.Builder(context);
        alert.setMessage("Are you sure?");

        alert.setPositiveButton("Stop", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                VerseService verseService = new VerseService(context);
                verseService.dropTable(language);
                callback.onComplete();
                Toast.makeText(context, "Installing "+language.getTitle() + " was cancelled", Toast.LENGTH_SHORT).show();
            }
        });

        alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        return alert;
    }

    public interface Callback {
        void onComplete();
    }
}
