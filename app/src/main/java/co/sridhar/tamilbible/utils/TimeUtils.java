package co.sridhar.tamilbible.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Utility that generates time as per the format
 * of this application
 * Created by sridharrajs on 9/23/17.
 */

public class TimeUtils {

    public static String getFormat() {
        return "yyyy-MM-dd HH:mm:ss";
    }

    public static String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
}
