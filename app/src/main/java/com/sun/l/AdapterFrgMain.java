package com.sun.l;

import android.content.pm.ResolveInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.sun.l.widget.ITouchListener;
import com.sun.l.widget.LoopViewPager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by sunje on 2016-02-25.
 */
public class AdapterFrgMain extends FragmentPagerAdapter {

    private List mList = new ArrayList<ResolveInfo>();
    private HashMap<Integer, ArrayList<ResolveInfo>> mMap = new HashMap<>();
    private ITouchListener onTouchListener;
    private Callback callback;

    public interface Callback {
        void notifyDatasetChanged();
    }

    public void setCallback(Callback c) {
        this.callback = c;
    }

    public AdapterFrgMain(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        position = LoopViewPager.toRealPosition(position, getCount());
        Fragment frg = FrgL.newInstance(mMap.get(position), onTouchListener);
//        Fragment frg = FrgL.newInstance(mMap.get(realPosition), onTouchListener);
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
        mMap.clear();
        this.mMap.putAll(map);
        callback.notifyDatasetChanged();
        notifyDataSetChanged();
    }

    public void setOnTouchListener(ITouchListener l) {
        onTouchListener = l;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

}
