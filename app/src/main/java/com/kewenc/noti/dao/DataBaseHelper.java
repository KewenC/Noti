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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "noti.db";
    private static final int DATABASE_VERSION = 2;//必须大于等于2，因为onCreate永远不会执行，得保证首次安装应用onUpgrade执行在此数据库的基础上增加新表或字段等
    public static final String DATABASE_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/"+ BuildConfig.APPLICATION_ID +"/"+"databases"+"/";
    private final Context context;

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
        Log.e("TAGF","DataBaseHelper_onUpgrade_oldVersion_"+oldVersion+"_"+newVersion);
    }

    public SQLiteDatabase createDataBase(){
        String myPath = DATABASE_PATH + DATABASE_NAME;
        File file = new File(myPath);
        if (!file.exists()) {
            try {
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return getWritableDatabase();
    }

//
//    private boolean checkDataBase() {
//        String myPath = DATABASE_PATH + DATABASE_NAME;
//        File file = new File(myPath);
//        return file.exists();
////        SQLiteDatabase checkDB = null;
////        try {
////            String myPath = DATABASE_PATH + DATABASE_NAME;
////            Log.e("TAGF","checkDataBase_"+myPath);
////            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
////        } catch (Exception e) {
////            Log.e("TAGF","checkDataBase_Exception");
////            e.printStackTrace();
////        }
////        if (checkDB != null) {
////            checkDB.close();
////        }
////        return checkDB != null;
//    }

//    public SQLiteDatabase openDataBase() throws SQLException {
//        String myPath = DATABASE_PATH + DATABASE_NAME;
////        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
//                db = SQLiteDatabase.openOrCreateDatabase(myPath, null);
//        return db;
//    }

//    @Override
//    public synchronized void close() {
//        if (db != null)
//            db.close();
//        super.close();
//    }
}
