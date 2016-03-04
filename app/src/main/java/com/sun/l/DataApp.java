package com.sun.l;


import android.graphics.drawable.Drawable;

/**
 * Created by sunje on 2016-02-26.
 */
public class DataApp {
    int category = 0;

    String packageName = "";
    String label = "";

    int idIconRes = 0;
    Drawable icon = null;

    public DataApp(String label, String packageName, Drawable icon) {
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;
    }

    public DataApp(int category, String label, String packageName) {
        this.category = category;
        this.label = label;
        this.packageName = packageName;

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getIdIconRes() {
        return idIconRes;
    }

    public void setIdIconRes(int idIconRes) {
        this.idIconRes = idIconRes;
    }

    public String getPackageName() {
        return packageName;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public Drawable getIcon() {
        return icon;
    }
}