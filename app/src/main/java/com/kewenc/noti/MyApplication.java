/*
 * Copyright (c) 2019.7 - Present by KewenC.
 */

package com.kewenc.noti;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kewenc.noti.dao.DataBaseHelper;

import java.io.IOException;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        DataBaseHelper dataBaseHelper = new DataBaseHelper(this);
        try {
            dataBaseHelper.createDataBase();
        } catch (IOException e) {
            Log.e("TAGF","MyApplication_IOException");
            e.printStackTrace();
        }
        SQLiteDatabase dp = dataBaseHelper.openDataBase();
    }
}
