package co.sridhar.tamilbible.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppPreferences {

    private static String NIGHT_MODE_IDENTIFIER = "is_night_mode_1";
    private static String SECOND_LANG_MODE_IDENTIFIER = "is_show_second_lang";
    private static String DEFAULT_FONT_SIZE_IDENTIFIER = "default_font_size";
    private static String DEFAULT_LANGUAGE_IDENTIFIER = "default_language";
    private static String DEFAULT_TEXT_COLOR_IDENTIFIER = "default_text_color";
    private static String IS_LESS_SPACE_IDENTIFIER = "is_less_space";
    private static String DB_INSTALLATION_IDENTIFIER = "is_db_installation_in_progress";

    private static AppPreferences preferences;

    public static AppPreferences getInstance() {
        if (preferences == null) {
            preferences = new AppPreferences();
            return preferences;
        } else {
            return preferences;
        }
    }

    public boolean isLightMode(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        boolean isNightMode = getPrefs.getBoolean(NIGHT_MODE_IDENTIFIER, false);
        return !isNightMode;
    }

    public boolean isNightMode(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return getPrefs.getBoolean(NIGHT_MODE_IDENTIFIER, false);
    }

    public String getDefaultLanguage(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return getPrefs.getString(DEFAULT_LANGUAGE_IDENTIFIER, "tamil");
    }

    public void saveDefaultLanguage(Context context, String language) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putString(DEFAULT_LANGUAGE_IDENTIFIER, language);
        e.apply();
    }

    public void setStartDbInstallation(Context context) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putBoolean(DB_INSTALLATION_IDENTIFIER, true);
        e.apply();
    }

    public void setInstallLang(Context context, String language) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putString("process_language", language);
        e.apply();
    }

    public void clearInstallLang(Context context) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putString("process_language", "");
        e.apply();
    }

    public boolean isInstallLang(Context context) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return getPrefs.getString("process_language", "").equalsIgnoreCase("");
    }

    public boolean isInstallLangInProgress(Context context) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return !getPrefs.getString("process_language", "").equalsIgnoreCase("");
    }

    public String getInstallLang(Context context) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return getPrefs.getString("process_language", "");
    }

    public void setCompleteDbInstallation(Context context) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putString("process_language", "");
        e.apply();
    }

    public boolean isShowSecondLanguage(Context context) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        return getPrefs.getBoolean(SECOND_LANG_MODE_IDENTIFIER, false);
    }

    public void enableSecondLanguage(Context context) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putBoolean(SECOND_LANG_MODE_IDENTIFIER, true);
        e.apply();
    }

    public void disableSecondLanguage(Context context) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putBoolean(SECOND_LANG_MODE_IDENTIFIER, false);
        e.apply();
    }

    public float getFontSize(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return getPrefs.getFloat(DEFAULT_FONT_SIZE_IDENTIFIER, Defaults.getInstance().getDefaultFontSize());
    }

    public int getTextColor(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return getPrefs.getInt(DEFAULT_TEXT_COLOR_IDENTIFIER, Defaults.getInstance().getDefaultFontColor());
    }

    public void resetToDefaults(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putFloat(DEFAULT_FONT_SIZE_IDENTIFIER, Defaults.getInstance().getDefaultFontSize());
        e.putInt(DEFAULT_TEXT_COLOR_IDENTIFIER, Defaults.getInstance().getDefaultFontColor());
        e.putBoolean(IS_LESS_SPACE_IDENTIFIER, false);
        e.apply();
    }

    public void saveFontSize(Context mContext, float fontSize) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putFloat(DEFAULT_FONT_SIZE_IDENTIFIER, fontSize);
        e.apply();
    }

    public void saveFontColor(Context mContext, int fontColor) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putInt(DEFAULT_TEXT_COLOR_IDENTIFIER, fontColor);
        e.apply();
    }

    public void resetToDefaultTextColor(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putInt(DEFAULT_TEXT_COLOR_IDENTIFIER, Defaults.getInstance().getDefaultFontColor());
        e.apply();
    }

    public void saveNightMode(Context mContext, boolean isNightMode) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putBoolean(NIGHT_MODE_IDENTIFIER, isNightMode);
        e.apply();
    }

    public void markUpdateAsShowed(Context mContext, boolean isShowed) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putBoolean("update_notified", isShowed);
        e.apply();
    }

    public boolean isAppLoaded(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return getPrefs.getBoolean("is_app_loaded", false);
    }

    public boolean isUpdateNotifier(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return getPrefs.getBoolean("update_notified", false);
    }

    public void enableCompactView(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putBoolean(IS_LESS_SPACE_IDENTIFIER, true);
        e.apply();
    }

    public void disableCompactView(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putBoolean(IS_LESS_SPACE_IDENTIFIER, false);
        e.apply();
    }

    public boolean isLessSpaceView(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return getPrefs.getBoolean(IS_LESS_SPACE_IDENTIFIER, false);
    }

    public boolean isFirstLoad(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        return getPrefs.getBoolean("firstStart", true);
    }

    public void saveFirstLoad(Context mContext) {
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(mContext);
        SharedPreferences.Editor e = getPrefs.edit();
        e.putBoolean("firstStart", false);
        e.apply();
    }

    public String getPrimaryColor(Context mContext) {
        if (this.isNightMode(mContext)) {
            return "#546e7a";
        } else {
            return "#02b875";
        }
    }

}
