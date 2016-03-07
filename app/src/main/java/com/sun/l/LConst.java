package com.sun.l;

/**
 * Created by sunje on 2016-02-26.
 */
public class LConst {
    public static class Category {
        public static final int CAT_LOOK = 0;
        public static final int CAT_WRITE = 1;
        public static final int CAT_HEAR = 2;
        public static final int CAT_SEEK = 3;
    }

    public static class Request {
        public static final int Setting = 2342;
        public static final int Picture = 2343;
    }

    public static class Key {
        public static final String background = "background";
    }

    public static class PrefKey {
        public static final String sort = "p.sort";
    }

    public static class PrefValue {
        public static final String SORT_NAME = "p.v.sort.name";
        public static final String SORT_TIME = "p.v.sort.time";
        public static final String SORT_CUSTOM = "p.v.sort.custom";
        public static final String SORT_COLOR = "p.v.sort.color";
        public static final String SORT_RECOMMEND = "p.v.sort.recommend";
        public static final String SORT_DEFAULT = "p.v.sort.default";

    }
}
