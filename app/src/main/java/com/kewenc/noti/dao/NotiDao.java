package com.kewenc.noti.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kewenc.noti.model.CollectModel;
import com.kewenc.noti.model.DefaultModel;

import java.util.ArrayList;
import java.util.List;

public class NotiDao {

    public static final int FLAG_CET4 = 0;
    public static final int FLAG_CET6 = 1;
    public static final int FLAG_TEEFPS = 2;
    public static final int FLAG_IELTS = 3;
    public static final int FLAG_COLLECT = 4;

    public static final String TABLE_NAME_CET4 = "cet4";
    public static final String TABLE_NAME_CET6 = "cet6";
    public static final String TABLE_NAME_TEEFPS = "teefps";
    public static final String TABLE_NAME_IELTS = "ielts";
    public static final String TABLE_NAME_COLLECT = "collect";
    public static final String[] TABLES_NAME = {TABLE_NAME_CET4, TABLE_NAME_CET6, TABLE_NAME_TEEFPS, TABLE_NAME_IELTS,TABLE_NAME_COLLECT};
    private SQLiteDatabase db;

    public NotiDao(Context context){
        db = SQLiteDatabase.openOrCreateDatabase(DataBaseManager.DB_PATH, null);
    }

    /**
     * 查
     * @param flag
     * @return
     */
    public List query(int flag){
        if (flag == FLAG_COLLECT){
            List<CollectModel> collectModels = new ArrayList<CollectModel>();

            return collectModels;
        } else {
            List<DefaultModel> defaultModels = new ArrayList<DefaultModel>();
            Cursor c = db.query(TABLES_NAME[flag], null, null, null, null, null, null);
            if(c != null) {
                DefaultModel defaultModel = null;
                while(c.moveToNext()){
                    defaultModel = new DefaultModel();
                    defaultModel.setId(c.getInt(c.getColumnIndex("id")));
                    defaultModel.setWord(c.getString(c.getColumnIndex("word")));
                    defaultModel.setMarken(c.getString(c.getColumnIndex("marken")));
                    defaultModel.setMarkus(c.getString(c.getColumnIndex("markus")));
                    defaultModel.setTranslate(c.getString(c.getColumnIndex("translate")));
                    defaultModel.setMarkenpath(c.getString(c.getColumnIndex("markenpath")));
                    defaultModel.setMarkuspath(c.getString(c.getColumnIndex("markuspath")));
                    defaultModel.setSort(c.getInt(c.getColumnIndex("sort")));
                    defaultModel.setCollect(c.getInt(c.getColumnIndex("collect")));
                    defaultModels.add(defaultModel);
                }
                if (c != null){
                    c.close();
                    c = null;
                }
            }
            if (db != null)
                db.close();
            return defaultModels;
        }
    }
}
