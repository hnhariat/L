package com.sun.l.models;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.text.TextUtils;

import com.sun.l.LConst;
import com.sun.l.SimpleCallback;

/**
 * Created by sunje on 2016-04-08.
 * Thread에 의해 CRUD로직을{@link DBHelper} 호출 하는 클래스
 */
public class DBHandler {

    private static final int MANIPULATION_SELECT = 0;
    private static final int MANIPULATION_INSERT = 1;
    private static final int MANIPULATION_UPDATE = 2;
    private static final int MANIPULATION_DELETE = 3;

    private static DBHandler sHandler;
    private final Context mContext;
    private final DBHelper helper;
    private final SQLiteDatabase db;
    private String table;
    private String query;
    private int manipulation;
    private String where;

    public DBHandler(Context context) {
        this.mContext = context;
        this.helper = new DBHelper(context);
        this.db = helper.getWritableDatabase();

        helper.onCreate(db);
    }

    public static DBHandler open(Context context) throws SQLiteException {

        if (sHandler == null) {
            synchronized (DBHandler.class) {
                if (sHandler == null) {
                    sHandler = new DBHandler(context);
                }
            }
        }
        return sHandler;
    }

    public void drop(String tableName) {
        db.execSQL("drop table " + tableName);
    }

    public DBHandler select(String table) {
        this.table = table;
        this.manipulation = MANIPULATION_SELECT;
        return this;
    }

    public DBHandler where(String where) {
        this.where = where;
        return this;
    }

    public void execute(String query, SimpleCallback callback) {
        this.query = query;
        StringBuilder builder = new StringBuilder();
        builder.append("select * from ").append(table).append(TextUtils.isEmpty(where) ? "" : " where " + where);
        Cursor cursor = db.rawQuery(builder.toString(), null);
        callback.onResult(LConst.Return.SUCC, cursor, null);
    }

    public void execute(ContentValues cv, SimpleCallback callback) {
        int result = 0;
        switch (manipulation) {
            case MANIPULATION_INSERT:
                result = (int) db.insert(table, null, cv);
                break;
            case MANIPULATION_UPDATE:
                result = db.update(table, cv, where, null);
                break;
            case MANIPULATION_DELETE:
                result = db.delete(table, where, null);
                break;
        }

        callback.onResult(result, null, null);
    }
}
