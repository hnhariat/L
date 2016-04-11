package com.sun.l.models;

/**
 * Created by sunje on 2016-04-08.
 */
public class DataFolder {

    public static final int TYPE_NEW = 1231;
    public static final int TYPE_NORMAL = 1232;

    String name = "";
    int seq;
    int type = 0;

    public DataFolder(String name, int type, int seq) {
        this.name = name;
        this.seq = seq;
        this.type = type;
    }

    public boolean isFolder() {
        return type == TYPE_NORMAL;
    }

    public String getName() {
        return name;
    }

    public int getSeq() {
        return seq;
    }

    public int getType() {
        return type;
    }
}
