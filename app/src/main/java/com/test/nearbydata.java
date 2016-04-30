package com.test;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import nearbysite.nearbysites;

/**
 * Created by Administrator on 2016/4/7.
 */
public class nearbydata {
    private database helper;

    public nearbydata(Context context) {
        helper = new database(context);
    }

    //添加Nearby信息
    public void add(String name, String busid, String distance) {

        if (find(name) == false) {
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("insert into nearby (name,busid,distance) values(?,?,?)", new Object[]{name, busid, distance});
            db.close();
        } else {
            return;
        }

    }

    public boolean find(String name) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select *from nearby where name=?", new String[]{name});
        boolean result = cursor.moveToNext();
        cursor.close();
        db.close();
        return result;
    }

    public void updata(String name, String distance) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("update nearby set distance=？ where name=?", new Object[]{distance, name});
        db.close();
    }

    //删除一条记录
    public void delete(String name) {

        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from nearby where name=?", new Object[]{name});
        db.close();
    }

    public List<nearbysites> findAll() {
        SQLiteDatabase db = helper.getReadableDatabase();
        List<nearbysites> sites = new ArrayList<nearbysites>();
        Cursor cursor = db.rawQuery("select * from nearby", null);
        while (cursor.moveToNext()) {
            int id = cursor.getInt(cursor.getColumnIndex("id"));
            String name = cursor.getString(cursor.getColumnIndex("name"));
            String busid = cursor.getString(cursor.getColumnIndex("busid"));
            String distance = cursor.getString(cursor.getColumnIndex("distance"));
            nearbysites site = new nearbysites(id, name, busid, distance);
            sites.add(site);
        }

        cursor.close();
        db.close();

        return sites;
    }

    public void clear() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.execSQL("delete from nearby where 1=1");
        db.close();

    }


}
