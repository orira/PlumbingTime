package com.rsd.plumbing.util;

import android.graphics.Point;
import android.view.Display;

/**
 * Created by wadereweti on 3/12/13.
 */
public class DisplayUtil {

    public static int getWindowWidth(Display display) {
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    public static int getWindowHeight(Display display) {
        Point size = new Point();
        display.getSize(size);
        return size.y;
    }

    public static Point getWindowDimension(Display display) {
        Point size = new Point();
        display.getSize(size);
        return size;
    }
}
