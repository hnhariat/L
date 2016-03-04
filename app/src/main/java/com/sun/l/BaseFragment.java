package com.sun.l;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by sunje on 2016-02-25.
 */
public class BaseFragment extends Fragment implements IInitializer{
    protected View mRoot;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return mRoot;
    }

    protected void initialize(){
        initData();
        initView();
        initControl();
    }

    @Override
    public void initData() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void initControl() {

    }
}
