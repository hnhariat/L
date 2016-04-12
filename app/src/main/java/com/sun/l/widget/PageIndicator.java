package com.sun.l.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.sun.l.LUtils;

/**
 * Created by sunje on 2016-03-11.
 */
public class PageIndicator extends View {

    private final Paint mPaintStateNone;
    private final Paint mPaintStateSelected;
    private final int mScreenWidth;
    private final int mSizePagePoint;
    private int count = 0;
    private int currentPage;
    private float pageOffset;
    private int direction;

    public PageIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaintStateNone = new Paint();
        mPaintStateSelected = new Paint();
        mPaintStateNone.setColor(Color.LTGRAY);
        mPaintStateNone.setStyle(Paint.Style.FILL);

        mPaintStateSelected.setColor(Color.WHITE);
        mPaintStateSelected.setStyle(Paint.Style.FILL);

        mSizePagePoint = LUtils.dip2px(context, 9);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d("L.view.onDraw", "@PageIndicator");
        int start = 0;
        start = (mScreenWidth - (mSizePagePoint * count + (mSizePagePoint * (count - 1)))) / 2;
        Paint paint = null;
        for (int i = 0; i < count; i++) {
            Log.i("L.PagerIndicator", "L : " + (mScreenWidth - (mSizePagePoint * count)) / 2);
            paint = mPaintStateNone;
            canvas.drawRoundRect(start + ((i * 2) * mSizePagePoint), 0, start + mSizePagePoint + ((i * 2) * mSizePagePoint), mSizePagePoint, 50, 50, paint);
        }
        Log.d("sdkfjlsdfj", (pageOffset * mSizePagePoint) + "");
        canvas.drawRoundRect((pageOffset * mSizePagePoint * 2) + start + ((currentPage * 2) * mSizePagePoint), 0, (2 * pageOffset * mSizePagePoint) +
                start + mSizePagePoint + ((currentPage * 2) * mSizePagePoint), mSizePagePoint, 50, 50, mPaintStateSelected);
    }

    public void setPageCount(int count) {
        Log.d("L.pageindicator", "count : " + count);
        this.count = count;
        invalidate();
    }

    public void setSelecetedPage(int page) {
        pageOffset = 0;
        currentPage = page;
        Log.d("L.pageindicator.cur", "selected page : " + page);
        invalidate();
    }

    public void setPageOffset(int page, float offset, int direction) {
        this.pageOffset = offset;
        this.direction = direction;
        this.currentPage = page;
        Log.d("L.pageindicator.offset", "page : " + page + " offset : " + offset + " direction : " + (direction == 0 ? "to Prev" : "to next"));
        invalidate();
    }
}
