package com.sun.l;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sun.l.widget.ITouchListener;
import com.sun.l.widget.LIcon;
import com.sun.l.widget.LIconGridView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sunje on 2016-02-25.
 */
public class FrgL extends BaseFragment {

    private List<DataApp> listApps = new ArrayList<DataApp>();
    private LIconGridView viewIconPanel;
    private ITouchListener onTouchListener;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mRoot = View.inflate(getContext(), R.layout.fragment_icon_list, null);
        initialize();
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public static FrgL newInstance(List list, ITouchListener onTouchListener) {
        FrgL frg = new FrgL();
        frg.setListApps(list);
        frg.setOnTouchListener(onTouchListener);
        return frg;
    }

    public void setListApps(List list) {
        this.listApps.addAll(list);
    }

    @Override
    protected void initialize() {
        super.initialize();
        initIcon();
    }

    private void initIcon() {
        for (DataApp info : listApps) {
            final LIcon icon = new LIcon(getContext());
            icon.setAppInfo(info);
            icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getContext().getPackageManager().getLaunchIntentForPackage(icon.getAppInfo().getPackageName());
                    intent.setAction(Intent.ACTION_MAIN);
                    getContext().startActivity(intent);
                }
            });
            viewIconPanel.addView(icon);
        }
    }

    @Override
    public void initView() {
        super.initView();
        viewIconPanel = (LIconGridView) mRoot;
        viewIconPanel.setOnTouchListener(onTouchListener);
    }


    public void setOnTouchListener(ITouchListener onTouchListener) {
        this.onTouchListener = onTouchListener;
    }
}
