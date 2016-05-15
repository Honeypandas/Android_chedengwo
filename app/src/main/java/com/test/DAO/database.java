package com.test.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by Administrator on 2016/4/7.
 */
public class Database extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "wow.db";

    public Database(Context context) {

        super(context, DATABASE_NAME, null, 1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table nearby (id integer primary key autoincrement,name varchar(20),busid char(20),distance char(20))");
        String a = db.getPath();
        Log.e("db:", a);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
