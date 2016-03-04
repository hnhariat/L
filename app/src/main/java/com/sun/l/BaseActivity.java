package com.sun.l;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

/**
 * Created by sunje on 2016-02-25.
 */
public class BaseActivity extends AppCompatActivity implements IInitializer {
    protected View fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void initialize() {
        initData();
        initView();
        initControl();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        fragmentContainer = findViewById(R.id.fragment_container);
    }

    @Override
    public void initControl() {

    }

    protected void replaceFragment(BaseFragment frg, boolean isAttach) {
        if (fragmentContainer == null) {
            return;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.fragment_container, frg);
        ft.commitAllowingStateLoss();
    }
}
