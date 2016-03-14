package com.sun.l;

import android.annotation.TargetApi;
import android.app.WallpaperManager;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProviderInfo;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.Toast;

import com.sun.l.manager.FileManager;
import com.sun.l.utils.AnimationFactory;
import com.sun.l.utils.LBitmapCache;

import java.io.IOException;
import java.util.List;

public class LHomeActivity extends BaseActivity implements View.OnClickListener, ApplicationStateReceiver.OnApplicationStateListener {

    private ImageButton btnDetail;
    private LFragment frgDetail;
    private boolean isBackAnimationRunning;
    private View ctShortcut;
    private ApplicationStateReceiver applicationStateReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lhome);
        initialize();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow(); // in Activity's onCreate() for instance
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }
    }

    @Override
    protected void initialize() {
        super.initialize();
        initBackground();
        applicationStateReceiver = new ApplicationStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_INSTALL_PACKAGE);
        filter.addAction(Intent.ACTION_PACKAGE_ADDED);
        filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
        filter.addDataScheme("package");
        applicationStateReceiver.setOnApplicationStateListener(this);
        registerReceiver(applicationStateReceiver, filter);
    }

    private void initBackground() {

        Bitmap bmp = LBitmapCache.getInstance(getApplicationContext()).get(LConst.Key.background);
        if (bmp == null) {
            BitmapFactory.Options options = new BitmapFactory.Options();
            bmp = BitmapFactory.decodeFile(FileManager.getBackroundPath(), options);
            FileManager.toCache(getApplicationContext(), bmp);
        }
        if (bmp != null) {
            Drawable drawable = new BitmapDrawable(getResources(), bmp);
            findViewById(R.id.root).setBackgroundDrawable(drawable);
        } else {
            findViewById(R.id.root).setBackgroundResource(R.drawable.danji_melon);
        }
        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getApplicationContext());
        try {
            if (bmp == null) {
//                myWallpaperManager.setResource(R.drawable.danji_melon);
            } else {
                myWallpaperManager.setBitmap(bmp);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
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
        Log.d("L.key.event", keyCode+"");
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
                frgDetail.setSortOrder();
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(applicationStateReceiver);
    }

    @Override
    public void onInstall() {
        frgDetail.initApplicationList();
        Toast.makeText(this, "install", 0).show();
    }

    @Override
    public void onAdd() {
        frgDetail.initApplicationList();
        Toast.makeText(this, "add", 0).show();
    }

    @Override
    public void onRemove() {
        frgDetail.initApplicationList();
        Toast.makeText(this, "removed", 0).show();
    }
}
