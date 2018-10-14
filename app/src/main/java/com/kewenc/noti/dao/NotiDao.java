package com.kewenc.noti.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.kewenc.noti.model.CollectModel;
import com.kewenc.noti.model.DefaultModel;
import java.util.ArrayList;
import java.util.List;

import static com.kewenc.noti.dao.DataBaseManager.NOTI_DBNAME;

public class NotiDao {
    public static final int KEY_FLAG_MARK_EN_PATH = 0;
    public static final int KEY_FLAG_MARK_US_PATH = 1;
    public static final int KEY_FLAG_COLLECT = 2;
    public static final int KEY_FLAG_FALG = 3;

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
        db = SQLiteDatabase.openOrCreateDatabase("/data" + Environment.getDataDirectory().getAbsolutePath() + "/"+ context.getPackageName() +"/"+"databases"+"/"+NOTI_DBNAME, null);
    }

    public Cursor getCursor(int flag){
        return db.rawQuery("select id,word,marken,markus,translate,markenpath,markuspath,from "+TABLES_NAME[flag]+" where id=id", null);
        return db.query(TABLES_NAME[flag], null, "id=?", null, null, null, null);
    }

    public void closeDb(){
        if (db != null){
            db.close();
            db = null;
        }
    }

    /**
     * 增
     *
     * 适用范围： 仅CollectModel
     * @param collectModel
     * @return
     */
    public int insert(CollectModel collectModel){
        ContentValues values = new ContentValues();
        values.put("name", collectModel.getWord());
        values.put("name", collectModel.getMarken());
        values.put("name", collectModel.getMarkus());
        values.put("name", collectModel.getTranslate());
        values.put("name", collectModel.getMarkenpath());
        values.put("name", collectModel.getMarkuspath());
        values.put("name", collectModel.getFlag());
        long id = db.insert(TABLE_NAME_COLLECT, "0", values);
        if (db != null)
            db.close();
        return (int) id;
    }

    /**
     * 删
     *
     * 适用范围： 仅CollectModel
     * @param id
     * @return
     */
    public boolean delete(int id){
        int result = db.delete(TABLE_NAME_COLLECT, "id = ?", new String[]{String.valueOf(id)});
        if (db != null)
            db.close();
        return result>0;
    }

    /**
     * 改
     *
     * 适用范围： CollectModel 和 DefaultModel
     * @param flag
     * @param id
     * @param newStrValue
     * @param keyFlag
     * @return
     */
    public boolean update(int flag, int id, String newStrValue, int keyFlag){
        ContentValues values = new ContentValues();
        switch (keyFlag){
            case KEY_FLAG_MARK_EN_PATH:
                values.put("markenpath", newStrValue);
                break;
            case KEY_FLAG_MARK_US_PATH:
                values.put("markuspath", newStrValue);
                break;
            case KEY_FLAG_COLLECT:
                values.put("collect", newStrValue.equals("true"));//boolean
                break;
            case KEY_FLAG_FALG:
                values.put("flag", Integer.parseInt(newStrValue));//int
                break;
        }
        int result = db.update(TABLES_NAME[flag], values, "id=?", new String[]{String.valueOf(id)});
        if (db != null)
            db.close();
        return result>0;
    }

    /**
     * 查
     *
     * 适用范围： CollectModel 和 DefaultModel
     * @param flag
     * @return
     */
    public List query(int flag){
        Cursor c = db.query(TABLES_NAME[flag], null, null, null, null, null, null);
        if (flag == FLAG_COLLECT){
            List<CollectModel> collectModels = new ArrayList<CollectModel>();
            if(c != null) {
                CollectModel collectModel = null;
                while(c.moveToNext()){
                    collectModel = new CollectModel();
                    collectModel.setId(c.getInt(c.getColumnIndex("id")));
                    collectModel.setWord(c.getString(c.getColumnIndex("word")));
                    collectModel.setMarken(c.getString(c.getColumnIndex("marken")));
                    collectModel.setMarkus(c.getString(c.getColumnIndex("markus")));
                    collectModel.setTranslate(c.getString(c.getColumnIndex("translate")));
                    collectModel.setMarkenpath(c.getString(c.getColumnIndex("markenpath")));
                    collectModel.setMarkuspath(c.getString(c.getColumnIndex("markuspath")));
                    collectModel.setFlag(c.getInt(c.getColumnIndex("flag")));
                    collectModels.add(collectModel);
                }
                if (c != null){
                    c.close();
                    c = null;
                }
            }
            if (db != null)
                db.close();
            return collectModels;
        } else {
            List<DefaultModel> defaultModels = new ArrayList<DefaultModel>();
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
                    defaultModel.setCollect(c.getString(c.getColumnIndex("collect")).equals("true"));
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

    /**
     * 获取 表collect 的总记录数
     * @return 总记录数
     */
    public long getCount() {
        Cursor cursor = db.rawQuery("select count(id) from "+TABLE_NAME_COLLECT, null);
        if(cursor.moveToNext()) {
            long count = cursor.getLong(0);
            cursor.close();
            db.close();
            return count;//返回总记录数
        }
        cursor.close();
        db.close();
        return 0;
    }
}
