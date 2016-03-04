package com.sun.l;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by sunje on 2016-02-25.
 */
public class LUtils {
    public static int dip2px(Context context, int dip) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, context.getResources().getDisplayMetrics());
    }
}
