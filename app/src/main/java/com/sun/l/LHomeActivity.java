package com.sun.l;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ImageButton;

import com.sun.l.utils.AnimationFactory;
import com.sun.l.utils.LBitmapCache;

import java.util.List;

public class LHomeActivity extends BaseActivity implements View.OnClickListener {

    private ImageButton btnDetail;
    private LFragment frgDetail;
    private boolean isBackAnimationRunning;
    private View ctShortcut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhome);
        initialize();
    }

    @Override
    protected void initialize() {
        super.initialize();
        initBackground();
    }


    private void initBackground() {
        Bitmap bmp = LBitmapCache.getInstance(getApplicationContext()).get(LConst.Key.background);
        Drawable drawable = new BitmapDrawable(getResources(), bmp);
        findViewById(R.id.root).setBackground(drawable);
    }

    @Override
    public void initData() {
        super.initData();
        AppWidgetManager manager = AppWidgetManager.getInstance(this);
        List<AppWidgetProviderInfo> infoList = manager.getInstalledProviders();
        for (AppWidgetProviderInfo info : infoList) {
            Log.d("asdfsdf", "Name: " + info.loadLabel(getPackageManager()));
            Log.d("asdfsdf", "Provider Name: " + info.provider);
            Log.d("asdfsdf", "Configure Name: " + info.configure);
        }
    }

    @Override
    public void initView() {
        super.initView();
        btnDetail = (ImageButton) findViewById(R.id.btn_detail);
        frgDetail = new LFragment();
        ctShortcut = findViewById(R.id.ct_shortcut);
    }

    @Override
    public void initControl() {
        super.initControl();
        btnDetail.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.btn_detail) {
            if (ctShortcut.getVisibility() == View.VISIBLE) {
                isBackAnimationRunning = false;
                replaceFragment(frgDetail, true);
                Animation anim = AnimationFactory.fadeout(LHomeActivity.this, 300);
                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        ctShortcut.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                ctShortcut.setAnimation(anim);
                anim.start();
                fragmentContainer.setVisibility(View.VISIBLE);
                fragmentContainer.setAnimation(AnimationFactory.fadein(this, 300));
                fragmentContainer.getAnimation().start();
                ctShortcut.setVisibility(View.INVISIBLE);
            } else {

            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        replaceFragment(frgDetail, false);
        if (isBackAnimationRunning) {
            return true;
//            return super.onKeyDown(keyCode, event);
        }
        if (fragmentContainer.getVisibility() == View.VISIBLE) {
            frgDetail.resetAllViewState();
            isBackAnimationRunning = true;
            ctShortcut.startAnimation(AnimationFactory.fadein(this, 300));
            ctShortcut.getAnimation().setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ctShortcut.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            Animation anim = AnimationFactory.fadeout(this, 300);
            fragmentContainer.startAnimation(anim);
            anim.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    fragmentContainer.setVisibility(View.INVISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
            anim.start();
            return true;
        } else {
            return true;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case LConst.Request.Setting:
                initBackground();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
