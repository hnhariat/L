package com.sun.l.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.sun.l.DataApp;
import com.sun.l.LUtils;

/**
 * Created by sunje on 2016-02-25.
 */
public class LIconGridView extends ViewGroup {
    private final int TOUCH_MODE_SELECT = 21;
    private final int TOUCH_MODE_NONE = 2;
    private final RectF mIdelAreaRect;
    private final int dp16;
    private final int dp24;
    private RectF mTouchAreaRect;

    private int mTouchMode = TOUCH_MODE_NONE;

    private int mScreenWidth;
    private int mIconWidth;
    private int mViewMaxWidth;
    private int mWidthPadding;
    private GestureDetector gd;
    private final View[][] arrIconPosition = new View[50][4];
    private int mScreenHeight;
    private int heightStatusBar;

    private PointF pntFirst = new PointF();
    private PointF pntReal = new PointF();
    private PointF pntVisual = new PointF();

    private float distRatio = 1.3f;
    ITouchListener onTouchListener;
    private View mCurrentFocusedIconView;
    private int heightIconView;

    public LIconGridView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mViewMaxWidth = mScreenWidth;//- LUtils.dip2px(context, 8);
        mIconWidth = LUtils.dip2px(context, 72);
        mWidthPadding = (mViewMaxWidth - (mIconWidth * 4)) / 3;
        dp16 = LUtils.dip2px(context, 16);
        dp24 = LUtils.dip2px(context, 24);
        mTouchAreaRect = new RectF();
        mTouchAreaRect.set(mScreenWidth * 0.7f, mScreenHeight - (mScreenWidth - mScreenWidth * 0.7f), mScreenWidth - dp24, mScreenHeight - dp24);

        mIdelAreaRect = new RectF();
        gd = new GestureDetector(getContext(), gestureListener);
        for (int i = 0; i < 50; i++) {
            arrIconPosition[i] = new View[4];
        }

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(displayMetrics);

        heightStatusBar = LUtils.getStatusBarHeight(getContext());
        requestDisallowInterceptTouchEvent(true);

    }

    public void setOnTouchListener(ITouchListener l) {
        onTouchListener = l;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        // Measurement will ultimately be computing these values.
        int maxHeight = 0;
        int maxWidth = 0;
        int childState = 0;
        int mLeftWidth = 0;
        int rowCount = 0;

        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE)
                continue;

            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            maxWidth += Math.max(maxWidth, child.getMeasuredWidth());
            mLeftWidth += child.getMeasuredWidth();

            if ((mLeftWidth / mScreenWidth) > rowCount) {
                maxHeight += child.getMeasuredHeight();
                rowCount++;
            } else {
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight());
            }
            childState = combineMeasuredStates(childState, child.getMeasuredState());
        }

//        maxHeight = (int) (Math.ceil((count + mDateOfWeek - 1) / 7d) * (mWidthDate * 0.75));// 요일중 일요일이 1부터 시작하므로 1을 빼줌
        maxHeight = getMeasuredHeight();//getSuggestedMinimumHeight();
        maxWidth = Math.max(maxWidth, getSuggestedMinimumWidth());
        int expandSpec = MeasureSpec.makeMeasureSpec(MEASURED_SIZE_MASK, MeasureSpec.EXACTLY);

        setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, childState),
                resolveSizeAndState(maxHeight, expandSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));

        CustomViewPager.LayoutParams params = (CustomViewPager.LayoutParams) getLayoutParams();
        params.height = getMeasuredHeight();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        final int count = getChildCount();
        int curWidth, curHeight, curLeft, curTop, maxHeight;

        final int childLeft = this.getPaddingLeft();
        final int childTop = this.getPaddingTop();
        final int childRight = this.getMeasuredWidth() - this.getPaddingRight();
        final int childBottom = this.getMeasuredHeight() - this.getPaddingBottom();
        final int childWidth = childRight - childLeft;
        final int childHeight = childBottom - childTop;

        maxHeight = 0;
        curLeft = childLeft;
        curTop = childTop + heightStatusBar;
        int paddingLeft = 0, paddingTop = 0;
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                return;
            }

            child.measure(MeasureSpec.makeMeasureSpec(mIconWidth, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.AT_MOST));
            curWidth = mIconWidth;
            curHeight = mIconWidth;

            if (curLeft + curWidth >= childRight) {
                curLeft = childLeft;
                curTop += maxHeight + mWidthPadding;
                maxHeight = 0;
            }

            if (i == 4) {
                curLeft = 0;
            }
            if (i % 4 == 0) {
                paddingLeft = 0;
            } else {
                paddingLeft = mWidthPadding;
            }
            child.layout(paddingLeft + curLeft, paddingTop + curTop, paddingLeft + curLeft + curWidth, paddingTop + curTop + curHeight);

            if (maxHeight < curHeight) {
                maxHeight = curHeight;
            }
            Log.d("asldfjlsdfjsldf", "count : " + count + " | " + i);
            arrIconPosition[i / 4][i % 4] = child;
            curLeft += curWidth + paddingLeft;
            heightIconView = paddingTop + curTop + curHeight;
        }
        mIdelAreaRect.set(0, heightIconView, mScreenWidth, mScreenHeight);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        gd.onTouchEvent(event);

        pntReal.set(event.getRawX(), event.getRawY());
        Log.i("L.icon", mTouchMode == TOUCH_MODE_SELECT ? "touch mode select" : "touch mode none");
        Log.i("L.icon", "event : " + event.getAction());
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pntFirst.set(event.getRawX(), event.getRawY());
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mTouchMode == TOUCH_MODE_SELECT) {
                    setPositionMapping(pntFirst, pntReal);
                    LIcon focusedView = (LIcon) getCurrentAppInfo(pntVisual);
                    if (focusedView == null) {
                        if (mCurrentFocusedIconView != null) {
                            releaseView(mCurrentFocusedIconView);
                        }
                        Log.i("L.icon", "|||||||||||||||||||||||||");
                    }
                    if (focusedView != mCurrentFocusedIconView) {
                        Log.e("L.icon", "cur : " + (focusedView != null ? focusedView.getAppInfo().getLabel() : "") +
                                " prev : " + (mCurrentFocusedIconView != null ? ((LIcon) mCurrentFocusedIconView).getAppInfo().getLabel() : ""));
                        Log.i("L.icon", "--------------------------");
                        releaseView(mCurrentFocusedIconView);
                        if (focusedView != null) {
                            ((LIcon) focusedView).setIconFocus(true);
                            mCurrentFocusedIconView = focusedView;
                            mCurrentFocusedIconView.invalidate();
                        } else {
                            mCurrentFocusedIconView = null;
                        }
                    }
                } else if (mTouchMode == TOUCH_MODE_NONE) {
                    Log.d("dksjf", "sdfsdfsdf");
                    if (pntFirst.y - event.getRawY() > 120) {
                        // drag up
                        onTouchListener.onSlideUp(this);
                    } else if (pntFirst.y - event.getRawY() < -120) {
                        onTouchListener.onSlideDown(this);
                    }
                }
                return true;
            case MotionEvent.ACTION_UP:
                if (mTouchMode == TOUCH_MODE_SELECT) {
                    View finalFocusedView = getCurrentAppInfo(pntVisual);
                    mCurrentFocusedIconView = null;
                    if (finalFocusedView != null) {
                        String pkg = ((LIcon) finalFocusedView).getAppInfo().getPackageName();
                        if (TextUtils.isEmpty(pkg)) {
                            return true;
                        }
                        Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(pkg);
                        intent.setAction(Intent.ACTION_MAIN);
                        getContext().startActivity(intent);
                        releaseView(finalFocusedView);
                    }
                    onTouchListener.onLongPress(LIconGridView.this);
                } else if (mTouchMode == TOUCH_MODE_NONE) {
                    if (!mIdelAreaRect.contains(pntFirst.x, pntFirst.y)) {
                        View finalFocusedView = getCurrentPositionAppInfo(pntReal);
                        if (finalFocusedView != null) {
                            String pkg = ((LIcon) finalFocusedView).getAppInfo().getPackageName();
                            if (TextUtils.isEmpty(pkg)) {
                                return true;
                            }
                            Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(pkg);
                            intent.setAction(Intent.ACTION_MAIN);
                            getContext().startActivity(intent);
                            releaseView(finalFocusedView);
                        }
                    }
                }
                mTouchMode = TOUCH_MODE_NONE;
                return true;
        }

        return super.onTouchEvent(event);
    }

    GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        @Override
        public boolean onDown(MotionEvent e) {
            Log.d("L.view.touch", "onDown");
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.d("L.view.touch", "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.d("L.view.touch", "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            Log.d("L.view.touch", "onScroll");
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.d("L.view.touch", "onLongPress");
            mTouchMode = TOUCH_MODE_SELECT;
            if (onTouchListener != null) {
                onTouchListener.onLongPress(LIconGridView.this);
                releaseView(mCurrentFocusedIconView);
            }
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            Log.d("L.view.touch", "onFling");
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            onTouchListener.onDoubleTap(e);
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
    };

    private void releaseView(View view) {
        if (view != null) {
            ((LIcon) view).setIconFocus(false);
            view.invalidate();
        }
    }

    public void setPositionMapping(PointF firstPoint, PointF realPoint) {

        if (realPoint.x > mTouchAreaRect.left && realPoint.y > mTouchAreaRect.top && realPoint.x < mTouchAreaRect.right) {
            float ratioX, ratioY;

            ratioX = (realPoint.x - mTouchAreaRect.left) / (mTouchAreaRect.right - mTouchAreaRect.left);
            ratioY = (realPoint.y - mTouchAreaRect.top) / (mTouchAreaRect.bottom - mTouchAreaRect.top);


            pntVisual.set(mScreenWidth * ratioX, (mScreenHeight - heightStatusBar) * ratioY);
        } else {
            Log.e("L.view.touch.xy", "out of bounds");
            pntVisual.set(-1, -1);
        }

        Log.d("L.view.touch.xy", "(" + pntVisual.x + "," + pntVisual.y + ")");
    }

    private View getCurrentAppInfo(PointF visualPoint) {
        Log.d("L.view.touch.xy", "(" + visualPoint.x + "," + visualPoint.y + ")");
//        int left = LUtils.dip2px(getContext(), 4);
        int right = mViewMaxWidth - LUtils.dip2px(getContext(), 4);
        int top = LUtils.dip2px(getContext(), 4);

        int a = 0, b = 0;
        if (visualPoint.x < 0) {
            a = -1;
        } else if (visualPoint.x > 0 && visualPoint.x < mIconWidth + mWidthPadding * 0.5f) {
            // 1열
            a = 0;
        } else if (visualPoint.x > mIconWidth + mWidthPadding * 0.5f && visualPoint.x < mIconWidth * 2 + mWidthPadding * 1.5f) {
            a = 1;
            // 2열
        } else if (visualPoint.x > mIconWidth * 2 + mWidthPadding * 1.5f && visualPoint.x < mIconWidth * 3 + mWidthPadding * 2.5f) {
            a = 2;
            // 3열
        } else if (visualPoint.x > mIconWidth * 3 + mWidthPadding * 2.5f && visualPoint.x < right) {
            a = 3;
            // 4열
        } else if (visualPoint.x > right) {
            a = 3;
        } else {
            a = -1;
        }
        b = getIndexY(visualPoint);
        Log.d("L.array", "a : " + a + " b : " + b);
        if (a < 0 || b < 0) {
            return null;
        }
        View view = arrIconPosition[b][a];
        if (view == null) {
            return null;
        }
        DataApp info = ((LIcon) view).getAppInfo();
        Log.d("L.appinfo", "name : " + info.getLabel());
        return view;
    }

    private int getIndexY(PointF visualPoint) {
        int topPadding = LUtils.dip2px(getContext(), 4);
        topPadding = getPaddingTop() + heightStatusBar;
        int defaultSpaceY = heightStatusBar + topPadding;
        if (visualPoint.y < 0) {
            return -1;
        } else if (visualPoint.y > defaultSpaceY && visualPoint.y < defaultSpaceY + mIconWidth + (mWidthPadding * 0.5f)) {
            return 0;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth + mWidthPadding * 0.5f && visualPoint.y < defaultSpaceY + mIconWidth * 2 + mWidthPadding * 1.5f) {
            return 1;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 2 + mWidthPadding * 1.5f && visualPoint.y < defaultSpaceY + mIconWidth * 3 + mWidthPadding * 2.5f) {
            return 2;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 3 + mWidthPadding * 2.5f && visualPoint.y < defaultSpaceY + mIconWidth * 4 + mWidthPadding * 3.5f) {
            return 3;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 4 + mWidthPadding * 3.5f && visualPoint.y < defaultSpaceY + mIconWidth * 5 + mWidthPadding * 4.5f) {
            return 4;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 5 + mWidthPadding * 4.5f && visualPoint.y < defaultSpaceY + mIconWidth * 6 + mWidthPadding * 5.5f) {
            return 5;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 6 + mWidthPadding * 5.5f && visualPoint.y < defaultSpaceY + mIconWidth * 7 + mWidthPadding * 6.5f) {
            return 6;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 7 + mWidthPadding * 6.5f && visualPoint.y < defaultSpaceY + mIconWidth * 8 + mWidthPadding * 7.5f) {
            return 7;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 8 + mWidthPadding * 7.5f) {
            return -1;
        }
        return -1;
    }

    private View getCurrentPositionAppInfo(PointF visualPoint) {
        Log.d("L.view.touch.xy", "(" + visualPoint.x + "," + visualPoint.y + ")");
        int left = LUtils.dip2px(getContext(), 4);
        int right = mViewMaxWidth - LUtils.dip2px(getContext(), 4);
        int top = LUtils.dip2px(getContext(), 4);
        left = 0;
        right = 0;
        int a = 0, b = 0;
        if (visualPoint.x > left && visualPoint.x < left + mIconWidth + mWidthPadding) {
            // 1열
            a = 0;
        } else if (visualPoint.x > left + mIconWidth + mWidthPadding && visualPoint.x < left + mIconWidth * 2 + mWidthPadding) {
            a = 1;
            // 2열
        } else if (visualPoint.x > left + mIconWidth * 2 + mWidthPadding * 2f && visualPoint.x < left + mIconWidth * 3 + mWidthPadding * 2) {
            a = 2;
            // 3열
        } else if (visualPoint.x > left + mIconWidth * 3 + mWidthPadding * 3f && visualPoint.x < right) {
            a = 3;
            // 4열
        } else if (visualPoint.x > right) {
            a = 3;
        } else {
            a = -1;
        }
        b = getCurrentPositionIndexY(visualPoint);
        Log.d("L.array", "a : " + a + " b : " + b);
        if (a < 0 || b < 0) {
            return null;
        }
        View view = arrIconPosition[b][a];
        if (view == null) {
            return null;
        }
        DataApp info = ((LIcon) view).getAppInfo();
        Log.d("L.appinfo", "name : " + info.getLabel());
        return view;
    }

    private int getCurrentPositionIndexY(PointF visualPoint) {
        int topPadding = LUtils.dip2px(getContext(), 4);
        topPadding = getPaddingTop() + heightStatusBar;
        int defaultSpaceY = heightStatusBar + topPadding;
        if (visualPoint.y < 0) {
            return -1;
        } else if (visualPoint.y > defaultSpaceY && visualPoint.y < defaultSpaceY + mIconWidth) {
            return 0;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth + mWidthPadding && visualPoint.y < defaultSpaceY + mIconWidth * 2 + mWidthPadding) {
            return 1;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 2 + mWidthPadding * 2f && visualPoint.y < defaultSpaceY + mIconWidth * 3 + mWidthPadding * 2f) {
            return 2;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 3 + mWidthPadding * 3f && visualPoint.y < defaultSpaceY + mIconWidth * 4 + mWidthPadding * 3f) {
            return 3;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 4 + mWidthPadding * 4f && visualPoint.y < defaultSpaceY + mIconWidth * 5 + mWidthPadding * 4f) {
            return 4;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 5 + mWidthPadding * 5f && visualPoint.y < defaultSpaceY + mIconWidth * 6 + mWidthPadding * 5f) {
            return 5;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 6 + mWidthPadding * 6f && visualPoint.y < defaultSpaceY + mIconWidth * 7 + mWidthPadding * 6f) {
            return 6;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 7 + mWidthPadding * 7f && visualPoint.y < defaultSpaceY + mIconWidth * 8 + mWidthPadding * 7f) {
            return 7;
        } else if (visualPoint.y > defaultSpaceY + mIconWidth * 8 + mWidthPadding * 8f) {
            return -1;
        }
        return -1;
    }

}
