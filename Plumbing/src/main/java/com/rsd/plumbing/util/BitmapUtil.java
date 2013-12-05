package com.rsd.plumbing.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.rsd.plumbing.R;

/**
 * Created by wadereweti on 29/11/13.
 */
public class BitmapUtil {
    public static Bitmap getBitMap(int position, Context context) {
        int drawableId = getDrawableId(position);
        return BitmapFactory.decodeResource(context.getResources(), drawableId);
    }

    private static int getDrawableId(int position) {
        int imageId;

        switch (position) {
            case 0:
                imageId = R.drawable.pipes1;
                break;
            case 1:
                imageId = R.drawable.pipes2;
                break;
            case 2:
                imageId = R.drawable.pipes3;
                break;
            case 3:
                imageId = R.drawable.pipes4;
                break;
            case 4:
                imageId = R.drawable.pipes5;
                break;
            case 5:
                imageId = R.drawable.pipes6;
                break;
            case 6:
                imageId = R.drawable.pipes7;
                break;
            default:
                imageId = 0;
        }

        return imageId;
    }

}
