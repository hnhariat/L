package com.sun.l.utils;

import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by sunje on 2016-02-15.
 */
public class SortOrderName implements Comparator<ResolveInfo> {

    private final PackageManager pm;
    private transient Collator mColloator;

    public SortOrderName(PackageManager pm) {
        this.pm = pm;
    }


    @Override
    public int compare(ResolveInfo lhs, ResolveInfo rhs) {
        if (mColloator == null) {
            mColloator = Collator.getInstance();
        }
        return mColloator.compare(lhs.loadLabel(pm), rhs.loadLabel(pm));
//        if (lhs.get() > rhs.getTimeLastUpdated()) {
//            return 1;
//        } else if (lhs.getTimeLastUpdated() < rhs.getTimeLastUpdated()) {
//            return -1;
//        } else {
//            return 0;
//        }
    }
}
