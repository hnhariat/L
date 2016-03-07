package com.sun.l.utils;

import com.sun.l.DataApp;

import java.text.Collator;
import java.util.Comparator;

/**
 * Created by sunje on 2016-02-15.
 */
public class SortOrderCustom implements Comparator<DataApp> {

    private transient Collator mColloator;

    public SortOrderCustom() {

    }


    @Override
    public int compare(DataApp lhs, DataApp rhs) {
        if (mColloator == null) {
            mColloator = Collator.getInstance();
        }
        return mColloator.compare(lhs.getLabel(), rhs.getLabel());
//        if (lhs.get() > rhs.getTimeLastUpdated()) {
//            return 1;
//        } else if (lhs.getTimeLastUpdated() < rhs.getTimeLastUpdated()) {
//            return -1;
//        } else {
//            return 0;
//        }
    }
}
