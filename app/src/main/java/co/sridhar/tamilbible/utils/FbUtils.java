package co.sridhar.tamilbible.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class FbUtils {

    private static String FACEBOOK_URL = "https://www.facebook.com/onebible.in";
    private static String FACEBOOK_PAGE_ID = "388274841569942";

    public static String getFacebookPageURL(Context context) {
        PackageManager packageManager = context.getPackageManager();
        try {
            int versionCode = packageManager.getPackageInfo("com.facebook.katana", 0).versionCode;
            if (versionCode >= 3002850) { //newer versions of fb app
                return "fb://facewebmodal/f?href=" + FACEBOOK_URL;
            } else { //older versions of fb app
                return "fb://page/" + FACEBOOK_PAGE_ID;
            }
        } catch (PackageManager.NameNotFoundException e) {
            return FACEBOOK_URL; //normal web url
        }
    }

}
