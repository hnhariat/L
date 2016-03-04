package com.sun.l;

import android.content.pm.ResolveInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sun.l.widget.ITouchListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sunje on 2016-02-25.
 */
public class AdapterFrgMain extends FragmentStatePagerAdapter {

    private List mList = new ArrayList<ResolveInfo>();
    private HashMap<Integer, ArrayList<ResolveInfo>> mMap = new HashMap<>();
    private ITouchListener onTouchListener;

    public AdapterFrgMain(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        Fragment frg = FrgL.newInstance(mMap.get(position), onTouchListener);
        return frg;
    }

    @Override
    public int getCount() {
        return mMap.size();
    }

    public void setList(List list) {
//        this.mList.addAll(list);
        notifyDataSetChanged();
    }

    public void setList(HashMap map) {
        this.mMap.putAll(map);
        notifyDataSetChanged();
    }

    public void setOnTouchListener(ITouchListener l) {
        onTouchListener = l;
    }
}
