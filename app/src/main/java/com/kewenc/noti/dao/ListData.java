/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.dao;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kewenc.noti.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by KewenC on 2017/1/1.
 */

public class ListData {
//    private static SQLiteDatabase database;
    public static ArrayList<Map<String, Object>> list;
    public static OverrideSimpleAdapter simpleAdapter;
    public static int array_id[];
    public static String array_word[];
    public static String array_mark[];
    public static String array_translates[];
//    private static DataBaseOpenHelper helper;//创建DBOpenHelper对象
//    private static SQLiteDatabase db;//SQLiteDatabase对象

    /**
     * 获得listview数据
     * @param flag
     * @return
     */
    private static void DoData(int flag,Context context){
        NotiDao notiDao = new NotiDao(context);

        list = new ArrayList<Map<String, Object>>();
        Cursor cur = null;
        SharedPreferences sp=context.getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
        cur = notiDao.getCursor(flag);

//        switch (flag){
//            case 0:
//                database = SQLiteDatabase.openOrCreateDatabase(DataBaseManager.DB_PATH + "/" + DataBaseManager.CET4_DBNAME, null);
//                cur = database.rawQuery("select id,word,marken,markus,translate from cet4 where id=id", null);
//                break;
//            case 1:
//                database = SQLiteDatabase.openOrCreateDatabase(DataBaseManager.DB_PATH + "/" + DataBaseManager.CET6_DBNAME, null);
//                cur = database.rawQuery("select id,word,marken,markus,translate from cet6 where id=id", null);
//                break;
//            case 2:
//                database = SQLiteDatabase.openOrCreateDatabase(DataBaseManager.DB_PATH + "/" + DataBaseManager.TEEFPS_DBNAME, null);
//                cur = database.rawQuery("select id,word,marken,markus,translate from teefps where id=id", null);
//                break;
//            case 3:
//                database = SQLiteDatabase.openOrCreateDatabase(DataBaseManager.DB_PATH + "/" + DataBaseManager.IELTS_DBNAME, null);
//                cur = database.rawQuery("select id,word,marken,markus,translate from ielts where id=id", null);
//                break;
//            case 4:
//                helper=new DataBaseOpenHelper(context);//初始化DBOpenHelper对象
//                db=helper.getWritableDatabase();//初始化SQLiteDatabase对象
//                cur =db.rawQuery("select id,word,marken,markus,translate from tb_me where id=id", null);//存储到Cursor类中
//        }
            long count=cur.getCount();
            Map<String, Object> map;
            int index=0;
            array_id=new int[(int)count];
            array_word=new String[(int)count];
            array_mark=new String [(int)count];
            array_translates=new String[(int)count];
            String id;
            String word;
            String tmp;
            String tmp_en;
            String tmp_us;
            int tmp_num=sp.getInt("SETTING_NUM",0);
            if (cur != null) {
                if (cur.moveToFirst()) {
                    do {
                        id = cur.getString(cur.getColumnIndex("id"));
                        word = cur.getString(cur.getColumnIndex("word"));
                        tmp="";
                        tmp_en="";
                        tmp_us ="";
                        switch (tmp_num){
                            case 0:
                                tmp_en = cur.getString(cur.getColumnIndex("marken"));
                                break;
                            case 1:
                                tmp_us = cur.getString(cur.getColumnIndex("markus"));
                                break;
                            case 2:
                                tmp_en = cur.getString(cur.getColumnIndex("marken"));
                                tmp_us = cur.getString(cur.getColumnIndex("markus"));
                                break;
                            case 3:
                                break;
                        }
                        if (!tmp_en.equals("")){
                            if (!tmp_us.equals("")){
                                tmp="英 "+tmp_en+"  美 "+tmp_us+"\n";
                            }else{
                                tmp="英 "+tmp_en+"\n";
                            }
                        }else{
                            if (!tmp_us.equals("")){
                                tmp="美 "+tmp_us+"\n";
                            }else{

                            }
                        }
                        String translates= tmp+cur.getString(cur.getColumnIndex("translate"));//合并
                        array_id[index]=Integer.parseInt(id);
                        array_word[index]=word;
                        array_mark[index]=tmp;
                        array_translates[index]=cur.getString(cur.getColumnIndex("translate"));
                        index++;
                        map = new HashMap<String, Object>();
                        map.put("num", index);
                        map.put("word", word);
                        map.put("translates", translates);
                        list.add(map);
                    } while (cur.moveToNext());
                }
                cur.close();
                notiDao.closeDb();
//                if (database!=null){
//                    database.close();
//                }
//                if (db!=null){
//                    db.close();
//                }
//                return list;
            } else {
                if (cur!=null){
                    cur.close();
                }
                notiDao.closeDb();
//                if (database!=null){
//                    database.close();
//                }
//                if (db!=null){
//                    db.close();
//                }
//                return null;
            }
        }

    /**
     * SimpleAdapter适配器
     * @param context
     * @param flag
     * @return
     */
    public static OverrideSimpleAdapter DoAdapter(Context context,int flag){
        DoData(flag,context);
        simpleAdapter=new OverrideSimpleAdapter(context,list, R.layout.lv_item,
                      new String[]{"num","word","translates"},
                      new int[]{R.id.tv_num,R.id.tv_word,R.id.tv_translates});
        return simpleAdapter;
    }

}
