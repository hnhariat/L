package com.sun.l.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by sunje on 2016-02-15.
 */
public class SortOrderTime implements Comparator<ResolveInfo> {

    private final PackageManager pm;
    private transient Collator mColloator;

    public SortOrderTime(PackageManager pm) {
        this.pm = pm;
    }


    @Override
    public int compare(ResolveInfo lhs, ResolveInfo rhs) {
        if (mColloator == null) {
            mColloator = Collator.getInstance();
        }
        PackageInfo lInfo = null;
        PackageInfo rInfo = null;
        try {
            lInfo = pm.getPackageInfo(lhs.activityInfo.packageName, PackageManager.GET_META_DATA);
            rInfo = pm.getPackageInfo(rhs.activityInfo.packageName, PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (lInfo.firstInstallTime > rInfo.firstInstallTime) {
            return 1;
        } else if (lInfo.firstInstallTime < rInfo.firstInstallTime) {
            return -1;
        } else {
            return 0;
        }
    }
}
