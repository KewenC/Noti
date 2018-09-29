package com.kewenc.noti.dao;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import com.kewenc.noti.R;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by KewenC on 2016/12/27.
 */

public class DataBaseManager {
    private static final int BUFFER_SIZE = 400000;
    public static final String CET4_DBNAME = "cet4.db";// 保存的数据库文件名
    public static final String CET6_DBNAME = "cet6.db";
    public static final String TEEFPS_DBNAME = "teefps.db";
    public static final String IELTS_DBNAME = "ielts.db";
    public static final String PACKAGE_NAME = "com.kewenc.noti";
    public static final String DB_PATH = "/data" + Environment.getDataDirectory().getAbsolutePath() + "/" + PACKAGE_NAME; // 在手机里存放数据库的位置
//    private SQLiteDatabase database;
    private Context context;

    public DataBaseManager(Context context) {
        this.context = context;
    }

    public void openCet4Database() {
        openDatabase(DB_PATH + "/" + CET4_DBNAME, 0);
    }

    public void openCet6Database() {
        openDatabase(DB_PATH + "/" + CET6_DBNAME, 1);
    }

    public void openTeefpsDatabase() {
        openDatabase(DB_PATH + "/" + TEEFPS_DBNAME, 2);
    }

    public void openIeltsDatabase() {
//        this.database = this.openDatabase(DB_PATH + "/" + IELTS_DBNAME,3);
        openDatabase(DB_PATH + "/" + IELTS_DBNAME, 3);
    }

    private void openDatabase(String dbfile, int flag) {
        try {
            if (!(new File(dbfile).exists())) {// 判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = null;
                switch (flag) {
                    case 0:
                        is = this.context.getResources().openRawResource(R.raw.cet4); // 欲导入的数据库
                        break;
                    case 1:
                        is = this.context.getResources().openRawResource(R.raw.cet6); // 欲导入的数据库
                        break;
                    case 2:
                        is = this.context.getResources().openRawResource(R.raw.teefps); // 欲导入的数据库
                        break;
                    case 3:
                        is = this.context.getResources().openRawResource(R.raw.ielts); // 欲导入的数据库
                        break;
                    case 4:
                        is = this.context.getResources().openRawResource(R.raw.noti);
                }
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
//            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
//            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
//        return null;
        }
    }

    public void openNativeDatabase() {
        openDatabase(DB_PATH+"/"+"databases"+"/"+"noti.db",4);
    }
}
