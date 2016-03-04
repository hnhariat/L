package com.sun.l.widget;

import android.view.View;

/**
 * Created by sunje on 2016-02-26.
 */
public interface ITouchListener {
    void onLongPress(View view);
    void onIconClick(View view);
    void onSlideUp(View view);
    void onSlideDown(View view);
}
