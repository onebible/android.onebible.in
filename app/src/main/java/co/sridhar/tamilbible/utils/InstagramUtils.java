package co.sridhar.tamilbible.utils;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.util.Log;

public class InstagramUtils {

    public static Intent getIntent(PackageManager pm) {
        final Intent intent = new Intent(Intent.ACTION_VIEW);
        String url = "http://instagram.com/_u/onebible.in";
        try {
            if (pm.getPackageInfo("com.instagram.android", 0) != null) {
                intent.setData(Uri.parse(url));
                intent.setPackage("com.instagram.android");
                return intent;
            }
        } catch (PackageManager.NameNotFoundException ignored) {
            Log.e("instagram", ignored.toString());
        }
        intent.setData(Uri.parse(url));
        return intent;
    }

}
