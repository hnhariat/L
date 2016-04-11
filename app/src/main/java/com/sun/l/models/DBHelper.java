package com.sun.l.models;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by sunje on 2016-04-08.
 * query를 직접적으로 호출하는 클래스
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String NAME_DB = "llauncher.sqlite";
    private static final int VERSION = 0;

    public DBHelper(Context context) {
        super(context, NAME_DB, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "create table if not exists apps (" +
                "pkg text primary key, " +
                "seq integer, " +
                "name text not null, " +
                "time_last_updated long, " +
                "id_group integer);";
        db.execSQL(sql);

        sql = "create table if not exists group (" +
                "id integer primary key, " +
                "name text not null, " +
                "seq integer);";

        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int curVersion = oldVersion;
        try {
            db.beginTransaction();
            do {
                curVersion = upgrade(db, curVersion);
            } while (curVersion < newVersion);
            db.setTransactionSuccessful();
        } catch (Exception ignored) {
        } finally {
            db.endTransaction();
        }
    }

    private int upgrade(SQLiteDatabase db, int oldVersion) {
        switch (oldVersion) {
            case 1:
//                try {
//                    final String sql = "ALTER TABLE " + TABLE_APPOINTMENT + " ADD COLUMN creator_id INTEGER default -1;";
//                    db.execSQL(sql);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENT);
//                }

                return ++oldVersion;
            default:
                return VERSION;
        }
//        return VERSION;
    }
}
