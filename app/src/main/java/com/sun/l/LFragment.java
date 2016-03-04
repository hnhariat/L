package com.sun.l;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;

import com.sun.l.utils.AnimationFactory;
import com.sun.l.widget.ITouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LFragment extends BaseFragment implements ITouchListener, View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager pager;
    private AdapterFrgMain adapter;
    private List<ResolveInfo> listIntent;
    private boolean mLockAnimation;
    private int MODE_ACTIVE = 1;
    private int MODE_NONE = 0;
    private int mModeTouch = MODE_NONE;

    private HashMap<Integer, ArrayList<DataApp>> mapIconPage = new HashMap<>();
    private FloatingActionButton fab;
    private View viewShortcut;
    private Button btn1;
    private Button btn2;
    private Button btn3;
    private Button btn4;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_l, null);
        initialize();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initData() {
        super.initData();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        listIntent = getActivity().getApplicationContext().getPackageManager().queryIntentActivities(intent, 0);

        for (int i = 0; i < listIntent.size(); i++) {
            int page = i / 20;

            if (mapIconPage.size() == 0 || mapIconPage.get(page) == null) {
                mapIconPage.put(page, new ArrayList<DataApp>());
            }
            DataApp app = new DataApp(listIntent.get(i).loadLabel(getActivity().getApplicationContext().getPackageManager()).toString(), listIntent.get(i).activityInfo.packageName, listIntent.get(i).loadIcon(getActivity().getPackageManager()));
            mapIconPage.get(page).add(app);
        }
    }

    @Override
    public void initView() {
        super.initView();
        pager = (ViewPager) mRoot.findViewById(R.id.pager);
        pager.setPageMargin(LUtils.dip2px(getActivity().getApplicationContext(), 16));
        fab = (FloatingActionButton) mRoot.findViewById(R.id.btn);
        viewShortcut = mRoot.findViewById(R.id.view_shortcut);

        btn1 = (Button) viewShortcut.findViewById(R.id.btn1);
        btn2 = (Button) viewShortcut.findViewById(R.id.btn2);
        btn3 = (Button) viewShortcut.findViewById(R.id.btn3);
        btn4 = (Button) viewShortcut.findViewById(R.id.btn4);
    }

    @Override
    public void initControl() {
        super.initControl();
        adapter = new AdapterFrgMain(getChildFragmentManager());
        adapter.setOnTouchListener(this);
        pager.setAdapter(adapter);
        adapter.setList(mapIconPage);

        fab.setOnClickListener(this);
        pager.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn) {


            Animation anim = AnimationFactory.scale(getContext(), 1f, 0f, 1f, 0f, 200);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    fab.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(getActivity(), SettingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    getActivity().startActivityForResult(intent, LConst.Request.Setting);
//                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            fab.startAnimation(anim);
        }
    }

    @Override
    public void onLongPress(View view) {
        switchTouchMode();
    }

    @Override
    public void onIconClick(View view) {

    }

    @Override
    public void onSlideUp(View view) {
        Log.d("L.view.anim.hatti", "slide up");
        fab.setVisibility(View.VISIBLE);
        Animation anim = AnimationFactory.translateInterpolator(getContext(), 0, 0, 0, -fab.getHeight(), 200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                Animation anim = AnimationFactory.translateInterpolatorBounce(getContext(), 0, 0, -fab.getHeight(), 0, 300);
                fab.setAnimation(anim);
                anim.start();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.setAnimation(anim);
        anim.start();

//        Animation anim = AnimationFactory.fadein(getContext(), 500);
//        fab.setAnimation(anim);
//        anim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                fab.setAnimation(null);
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//        anim.start();
    }

    @Override
    public void onSlideDown(View view) {
        Log.d("L.view.anim.hatti", "slide down");
        if (fab.getVisibility() == View.INVISIBLE) {
            return;
        }
        Animation anim = AnimationFactory.fadeout(getContext(), 200);
        fab.startAnimation(anim);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
//        anim.start();
    }

    @Override
    public void onDoubleTap(MotionEvent e) {
//        viewShortcut.setVisibility(View.VISIBLE);
    }

    private void switchTouchMode() {
        if (mLockAnimation) {
            return;
        }
        if (mModeTouch == MODE_NONE) {
            mModeTouch = MODE_ACTIVE;
            pager.requestDisallowInterceptTouchEvent(true);
        } else {
            pager.requestDisallowInterceptTouchEvent(false);
            mModeTouch = MODE_NONE;
        }
        int from = mModeTouch == MODE_ACTIVE ? 0 : 178;
        int to = mModeTouch == MODE_ACTIVE ? 178 : 0;
        ValueAnimator animator = ValueAnimator.ofInt(from, to);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int alpha = (int) animation.getAnimatedValue();
                pager.setBackgroundColor(Color.argb(alpha, 0, 0, 0));
//                Log.d("alpha", alpha + "");
            }
        });
        animator.setDuration(250);
        animator.start();
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                mLockAnimation = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mLockAnimation = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void resetAllViewState() {
        if (fab.getVisibility() == View.INVISIBLE) {
            return;
        }
        Animation anim = AnimationFactory.scale(getContext(), 1f, 0, 1f, 0f, 200);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                fab.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        fab.setAnimation(anim);
        anim.start();
//        fab.startAnimation(anim);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        resetAllViewState();
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
