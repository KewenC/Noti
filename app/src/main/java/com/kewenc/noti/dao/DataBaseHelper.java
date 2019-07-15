/*
 * Copyright (c) 2019.7 - Present by KewenC.
 */

package com.kewenc.noti.dao;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;

import com.kewenc.noti.BuildConfig;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "noti.db";
    private static final int DATABASE_VERSION = 3;
//    public static final String DATABASE_PATH = "/data/data/" + getPackageName() + "/databases/";
//    public static final String DATABASE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + BuildConfig.APPLICATION_ID;
    public static final String DATABASE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/"+ BuildConfig.APPLICATION_ID +"/"+"databases"+"/";
    private final Context context;
    private SQLiteDatabase dp;

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.e("TAGF","DataBaseHelper_onCreate");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        Log.e("TAGF","dbExist_"+dbExist);
        if (dbExist) {
            //该数据库已经存在了
//            Log.e("TAGF","该数据库已经存在了");
        } else {
            //调用这个方法可以创建空数据库，我们自己的数据可可以将其覆盖,并设置版本号（不设置数据升级是个坑）
            this.getWritableDatabase().setVersion(DATABASE_VERSION);
            try {
                //覆盖
                String outFileName = DATABASE_PATH + DATABASE_NAME;
                InputStream is = context.getAssets().open(DATABASE_NAME);
                FileOutputStream fos = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024*4];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
                Log.e("TAGF","succeed");
            } catch (IOException e) {
                Log.e("TAGF","IOException");
                throw new Error("io error");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DATABASE_PATH + DATABASE_NAME;
            Log.e("TAGF","checkDataBase_"+myPath);
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null;
    }

    public SQLiteDatabase openDataBase() throws SQLException {
        //打开数据库
        String myPath = DATABASE_PATH + DATABASE_NAME;
        dp = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        return dp;
    }

    @Override
    public synchronized void close() {
        if (dp != null)
            dp.close();
        super.close();
    }
}
