package com.kewenc.noti.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.kewenc.noti.model.Tb_me;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by KewenC on 2016/12/29.
 */

public class MeDAO{
    private DataBaseOpenHelper helper;//创建DBOpenHelper对象
    private SQLiteDatabase db;//SQLiteDatabase对象
    public MeDAO (Context context)//定义构造函数
    {
        helper=new DataBaseOpenHelper(context.getApplicationContext());//初始化DBOpenHelper对象
    }
    /**
     * 添加信息
     *
     * @param tb_me
     */
    public void add(Tb_me tb_me)
    {

        db=helper.getWritableDatabase();//初始化SQLiteDatabase对象
        db.execSQL("insert into tb_me(id,word,marken,markus,translate)values(?,?,?,?,?)",new Object[]{tb_me.getId(),tb_me.getWord(),tb_me.getMarken(),tb_me.getMarkus(),tb_me.getTranslate()});//执行添加信息操作
        db.close();
    }
    /**
     * 更新信息
     *
     * @param tb_me
     */
    public void update(Tb_me tb_me)
    {
        db=helper.getWritableDatabase();//初始化SQLiteDatabase对象
        db.execSQL("update tb_me set word=?,marken=?,markus=?,translate=?where id=?",new Object[]{tb_me.getWord(),tb_me.getMarken(),tb_me.getMarkus(),tb_me.getTranslate(),tb_me.getId()});//执行修改信息操作
        db.close();
    }
    /**
     * 查找信息
     *
     * @param id
     * @return
     */
    public Tb_me find(int id)
    {
        db=helper.getWritableDatabase();//初始化SQLiteDatabase对象
        Cursor cursor=db.rawQuery("select id,word,marken,markus,translate from tb_me where id=?", new String[]{String.valueOf(id)});//根据编号查找信息，并存储到Cursor类中
        if(cursor.moveToNext())//遍历查找到的信息
        {
            Tb_me tb_me=new Tb_me(cursor.getInt(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("word")),cursor.getString(cursor.getColumnIndex("marken")),cursor.getString(cursor.getColumnIndex("markus")),cursor.getString(cursor.getColumnIndex("translate")));
            cursor.close();
            db.close();
            return tb_me;
        }
        cursor.close();
        db.close();
        return null;//如果没有信息，则返回null
    }
    /**
     * 删除信息
     *
     * @param ids
     */
    public void detele(Integer...ids)
    {
        if(ids.length>0)//判断是否存在要删除的id
        {
            StringBuffer sb=new StringBuffer();
            for(int i=0;i<ids.length;i++)//遍历要删除的id集合
            {
                sb.append("?").append(",");
            }
            sb.deleteCharAt(sb.length()-1);//去掉最后一个“,”字符
            db=helper.getWritableDatabase();
            db.execSQL("delete from tb_me where id in("+sb+")",(Object[])ids);//执行删除信息操作
            db.close();
        }
    }
    /**
     * 获取信息
     * @param start 起始位置
     * @param count 每页显示数量
     * @return
     */
    public List<Tb_me> getScrollData(int start, int count)
    {
        List<Tb_me>tb_me=new ArrayList<Tb_me>();//创建集合对象
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select *from tb_me limit ?,?",new String[]{String.valueOf(start),String.valueOf(count)});//获取所有的信息
        while(cursor.moveToNext())
        {
            tb_me.add(new Tb_me(cursor.getInt(cursor.getColumnIndex("id")),cursor.getString(cursor.getColumnIndex("word")),cursor.getString(cursor.getColumnIndex("marken")),cursor.getString(cursor.getColumnIndex("markus")),cursor.getString(cursor.getColumnIndex("translate"))));//将遍历到的信息添加到集合种
        }
        cursor.close();
        db.close();
        return tb_me;//返回集合
    }
    /**
     * 获取总记录数
     * @return
     */
    public long getCount()
    {
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select count(id) from tb_me", null);
        if(cursor.moveToNext())
        {
            long count=cursor.getLong(0);
            cursor.close();
            db.close();
            return count;//返回总记录数
        }
        cursor.close();
        db.close();
        return 0;
    }
    /**
     * 获取最大编号
     * @param
     */
    public int getMaxId()
    {
        db=helper.getWritableDatabase();
        Cursor cursor=db.rawQuery("select max(id) from tb_me", null);
        while(cursor.moveToLast())
        {
            int maxid=cursor.getInt(0);
            cursor.close();
            db.close();
            return maxid;
        }
        cursor.close();
        db.close();
        return 0;
    }

}

