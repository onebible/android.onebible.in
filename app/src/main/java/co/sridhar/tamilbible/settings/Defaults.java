package co.sridhar.tamilbible.settings;

import android.support.v7.app.AppCompatDelegate;

import co.sridhar.tamilbible.R;

public class Defaults {

    private static float DEFAULT_FONT_SIZE = 17;
    private static int MIN_SLIDER_VALUE = 13;

    private static int DEFAULT_FONT_COLOR = R.color.message;

    private static Defaults myDefaults;

    public static Defaults getInstance() {
        if (myDefaults == null) {
            myDefaults = new Defaults();
            return myDefaults;
        } else {
            return myDefaults;
        }
    }

    public float getDefaultFontSize() {
        return DEFAULT_FONT_SIZE;
    }

    public int getDefaultFontColor() {
        return DEFAULT_FONT_COLOR;
    }

    private static String HIGHLIGHT_COLOR_LIGHT_MODE = "#e0e0e0";
    private static String HIGHLIGHT_COLOR_DARK_MODE = "#eceff1";

    public String getHighlightingColor() {
        switch (AppCompatDelegate.getDefaultNightMode()) {
            case AppCompatDelegate.MODE_NIGHT_NO:
            default:
                return HIGHLIGHT_COLOR_LIGHT_MODE;
            case AppCompatDelegate.MODE_NIGHT_YES:
                return HIGHLIGHT_COLOR_DARK_MODE;
        }
    }

    public int getSliderMinValue() {
        return MIN_SLIDER_VALUE;
    }

}
