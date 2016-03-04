package com.sun.l.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;

import com.sun.l.R;

/**
 * Created by sunje on 2016-03-02.
 */
public class ConfigView extends View {
    private final int gravity;

    public ConfigView(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.IconGridView);
        gravity = a.getInt(R.styleable.IconGridView_layout_gravity, -1);
    }

    public int getGravity() {
        return gravity;
    }

}
