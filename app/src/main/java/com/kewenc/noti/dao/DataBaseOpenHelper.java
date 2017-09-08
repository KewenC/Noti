package com.kewenc.noti.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by KewenC on 2016/12/29.
 */

public class DataBaseOpenHelper extends SQLiteOpenHelper{
    private static final int VESAION=1;
    private static final String DBNAME="me.db";

    public DataBaseOpenHelper(Context context) {
        super(context, DBNAME, null, VESAION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table tb_me(id INTEGER primary key,word CHAR(50),marken CHAR(50),markus CHAR(50),translate CHAR(200))");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
