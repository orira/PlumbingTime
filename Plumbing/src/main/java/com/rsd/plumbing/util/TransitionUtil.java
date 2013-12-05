package com.rsd.plumbing.util;

import android.app.ActivityOptions;
import android.content.Context;
import android.os.Bundle;

import com.rsd.plumbing.R;

/**
 * Created by wadereweti on 28/11/13.
 */
public class TransitionUtil {
    public static Bundle returnSlideInBottomScaleAndAlphaOut(Context context) {
        return ActivityOptions.makeCustomAnimation(context, R.animator.slide_in_bottom, R.animator.fade_out_and_scale_down).toBundle();
    }

    public static Bundle returnScaleAndAlphaInAndSlideOutBottom(Context context) {
        return ActivityOptions.makeCustomAnimation(context, R.animator.fade_in_and_scale_up, R.animator.slide_out_bottom).toBundle();
    }
}
