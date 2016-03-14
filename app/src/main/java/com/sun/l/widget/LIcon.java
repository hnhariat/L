package com.sun.l.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.sun.l.DataApp;
import com.sun.l.LUtils;

import java.util.Random;

/**
 * Created by sunje on 2016-02-25.
 */
public class LIcon extends TextView {

    private final int defaultIconSizePx = 48;
    private int backgroundcolor;
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Paint mPaintBackground = new Paint(Paint.ANTI_ALIAS_FLAG);
    private DataApp appInfo;
    private boolean isIconFocused;
    private int iconSize;
    int[] location = new int[2];
    private Rect touchRect = new Rect();

    public LIcon(Context context) {
        super(context);
        initialize();
    }


    private void initialize() {
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(40);
        mPaintBackground.setStyle(Paint.Style.FILL);
        mPaintBackground.setColor(Color.argb(102, 255, 255, 255));
        Random rand = new Random();
        backgroundcolor = Color.rgb(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255));
        iconSize = LUtils.dip2px(getContext(), 48);
        setGravity(Gravity.CENTER);
//        setBackgroundColor(backgroundcolor);
    }

    public LIcon(Context context, AttributeSet attrs) {

        super(context, attrs);
        initialize();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mPaint.setTextAlign(Paint.Align.LEFT);
        int xPos = 0;
        int yPos = (int) ((canvas.getHeight() / 2) - ((mPaint.descent() + mPaint.ascent()) / 2));
        if (isIconFocused) {
            canvas.drawRoundRect(0, 0, canvas.getWidth(), canvas.getHeight(), 50, 50, mPaintBackground);
        }
//        int iconBoundStart = canvas.getWidth() / 2 - iconSize / 2;
//        appInfo.getIcon().setBounds(iconBoundStart, 0, iconBoundStart + iconSize, iconSize);
//        Log.d("klsdfjlsdfj", appInfo.getIcon().getIntrinsicWidth() + "/" + LUtils.dip2px(getContext(), 48));
//        canvas.drawText(appInfo.getLabel(), 20, iconSize + 40, mPaint);
//        appInfo.getIcon().draw(canvas);
    }

    public void setIconFocus(boolean focused) {
        isIconFocused = focused;
    }

    public void setAppInfo(DataApp appInfo) {
        this.appInfo = appInfo;
        setText(appInfo.getLabel());
        setTextColor(Color.WHITE);
        appInfo.getIcon().setBounds(0, 0, iconSize, iconSize);
        setCompoundDrawables(null, appInfo.getIcon(), null, null);

    }

    public DataApp getAppInfo() {
        return appInfo;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w("L.icon.child", "child event : " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                appInfo.getIcon().setAlpha(153);
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                if (!inViewInBounds(this, event.getRawX(), event.getRawY())) {
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                appInfo.getIcon().setAlpha(255);
                invalidate();
                if (inViewInBounds(this, event.getRawX(), event.getRawY())) {
                    String pkg = getAppInfo().getPackageName();
                    if (TextUtils.isEmpty(pkg)) {
                        return true;
                    }
                    Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(pkg);
                    intent.setAction(Intent.ACTION_MAIN);
                    getContext().startActivity(intent);
                }
                if (getParent() instanceof LIconGridView) {
                    ((LIconGridView)getParent()).childCancelFocus();
                }
                break;
            case MotionEvent.ACTION_CANCEL:
                appInfo.getIcon().setAlpha(255);
                invalidate();
                break;
        }
        return true;
    }

    private boolean inViewInBounds(View view, float x, float y) {
        view.getDrawingRect(touchRect);
        view.getLocationOnScreen(location);
        touchRect.offset(location[0], location[1]);
        return touchRect.contains((int) x, (int) y);
    }
}
