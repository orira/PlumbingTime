package com.rsd.plumbing.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by wadereweti on 5/12/13.
 */
public class TypefaceUtil {
    private static final String PREFIX = "fonts/Roboto-";
    private static final String SUFFIX = ".ttf";

    private static final String THIN = "Thin";
    private static final String LIGHT = "Light";
    private static final String REGULAR = "Regular";
    private static final String BOLD = "Bold";
    private static final String ITALIC = "Italic";

    private enum Font {
        THN, LIGHT, REGULAR, BOLD, ITALIC
    }

    public static Typeface getRobotoThin(Context context) {
        return Typeface.createFromAsset(context.getAssets(), returnPath(Font.THN));
    }

    public static Typeface getRobotoLight(Context context) {
        return Typeface.createFromAsset(context.getAssets(), returnPath(Font.LIGHT));
    }

    public static Typeface getRobotoRegular(Context context) {
        return Typeface.createFromAsset(context.getAssets(), returnPath(Font.REGULAR));
    }

    public static Typeface getRobotoBold(Context context) {
        return Typeface.createFromAsset(context.getAssets(), returnPath(Font.BOLD));
    }

    private static String returnPath(Font font) {
        String typeface = null;

        switch (font) {
            case THN:
                typeface = THIN;
                break;
            case LIGHT:
                typeface = LIGHT;
                break;
            case REGULAR:
                typeface = REGULAR;
                break;
            case BOLD:
                typeface = BOLD;
                break;
            case ITALIC:
                typeface = ITALIC;
                break;
        }

        StringBuilder builder = new StringBuilder();
        builder.append(PREFIX).append(typeface).append(SUFFIX);

        return builder.toString();
    }
}
