/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */
package com.kewenc.noti.service;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;
import com.kewenc.noti.R;
import com.kewenc.noti.dao.DataBaseManager;
import com.kewenc.noti.dao.DataBaseOpenHelper;
import com.kewenc.noti.receiver.AlarmReceiver;

public class NotificationService extends Service {
    private SharedPreferences sp;
    private static final long TEN_MINUTE_Mill=300000;//定时时间间隔常量
    private String word="Notification";
    private String translate="n. 通知；通告；[法] 告示";
    private int listview_position=0;
    private Bitmap bitmap;
    public NotificationService() {
    }

    @Override
    public void onCreate() {//首次创建service调用且只调用一次
        super.onCreate();
        SetRepeatingTask();
    }

    /**
     * 设置定时任务，启动Notification
     */
    private void SetRepeatingTask() {
        Intent intent_repeating=new Intent(this, AlarmReceiver.class);
        intent_repeating.setAction("com.kewenc.noti.action.repeating");
        PendingIntent sender= PendingIntent.getBroadcast(this, 0, intent_repeating, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){//api>=23
            alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+TEN_MINUTE_Mill,sender);
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){//api>=19
            alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+TEN_MINUTE_Mill,sender);
        }else{//api<19
            alarm.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime()+TEN_MINUTE_Mill,TEN_MINUTE_Mill,sender);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {//每次 start service 都会调用
        word="Notification";
        translate="n. 通知；通告；[法] 告示";
        listview_position=0;
        sp=this.getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
        int access_flag=sp.getInt("ACCESS_FLAG",0);
        switch (access_flag){
            case 0:GetCet4()  ;break;
            case 1:GetCet6()  ;break;
            case 2:GetTeefps();break;
            case 3:GetIelts() ;break;
            case 4:GetMe()    ;break;
        }
        OpenNoti();
        return START_STICKY;//START_REDELIVER_INTENT
    }

    /**
     * 开启通知栏信息
     */
    private void OpenNoti() {
        Intent intent_next=new Intent(this, AssistService.class);//待处理。。。
        PendingIntent pendingIntent=PendingIntent.getService(this,0,intent_next,0);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setTicker(word+" "+translate)
                .setContentTitle(word)
                .setContentText(translate)
                .setSmallIcon(R.drawable.ticket)
                .setLargeIcon(bitmap)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(translate))//多行文本通知
                .setShowWhen(false)
                .setAutoCancel(false)
                .setOngoing(true)
                .setPriority(NotificationCompat.PRIORITY_MAX)//通知优先级
                .setContentIntent(pendingIntent);
        builder.addAction(R.drawable.noti_next,"",pendingIntent);
        Notification notification=builder.build();
        notification.flags |= Notification.FLAG_NO_CLEAR;//表示正在运行的服务
//        notification.flags=Notification.FLAG_ONGOING_EVENT;
//        notification.flags=Notification.FLAG_FOREGROUND_SERVICE;
        NotificationManager nm=(NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(5,notification);
//        startForeground(5,notification);
        stopSelf();
    }

    /**
     * 获得我的信息
     */
    private void GetMe() {
        bitmap= BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);
        DataBaseOpenHelper helper=new DataBaseOpenHelper(getApplicationContext());//初始化DBOpenHelper对象
        SQLiteDatabase db=helper.getWritableDatabase();//初始化SQLiteDatabase对象
        Cursor cur=db.rawQuery("select id,word,marken,markus,translate from tb_me where id=id", null);//存储到Cursor类中
        int flag=0;
        int tmp_num=sp.getInt("SETTING_NUM",0);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    if (sp.getInt("ME_POSITION",0)==flag){
                        word = cur.getString(cur.getColumnIndex("word"));
                        String tmp="";
                        String tmp_en="";
                        String tmp_us ="";
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
                        translate = tmp+cur.getString(cur.getColumnIndex("translate"));
                        listview_position=flag;
                    }
                    flag++;
                } while (cur.moveToNext());
            }
            cur.close();
            db.close();
        } else {
            cur.close();
            db.close();
        }
    }

    /**
     * 获得雅思信息
     */
    private void GetIelts() {
        bitmap= BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DataBaseManager.DB_PATH + "/" + DataBaseManager.IELTS_DBNAME, null);
        Cursor cur = database.rawQuery("select id,word,marken,markus,translate from ielts where id=id", null);
        int flag=0;
        int tmp_num=sp.getInt("SETTING_NUM",0);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    if (sp.getInt("IELTS_POSITION",0)==flag){
                        word = cur.getString(cur.getColumnIndex("word"));
                        String tmp="";
                        String tmp_en="";
                        String tmp_us ="";
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
                        translate = tmp+cur.getString(cur.getColumnIndex("translate"));
                        listview_position=flag;
                    }
                    flag++;
                } while (cur.moveToNext());
            }
            cur.close();
            database.close();
        } else {
            cur.close();
            database.close();
        }
    }

    /**
     * 获得六级信息
     */
    private void GetCet6() {
        bitmap= BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DataBaseManager.DB_PATH + "/" + DataBaseManager.CET6_DBNAME, null);
        Cursor cur = database.rawQuery("select id,word,marken,markus,translate from cet6 where id=id", null);
        int flag=0;
        int tmp_num=sp.getInt("SETTING_NUM",0);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    if (sp.getInt("CET6_POSITION",0)==flag){
                        word = cur.getString(cur.getColumnIndex("word"));
                        String tmp="";
                        String tmp_en="";
                        String tmp_us ="";
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
                        translate = tmp+cur.getString(cur.getColumnIndex("translate"));
                        listview_position=flag;
                    }
                    flag++;
                } while (cur.moveToNext());
            }
            cur.close();
            database.close();
        } else {
            cur.close();
            database.close();
        }
    }

    /**
     * 获得四级信息
     */
    private void GetCet4() {
        bitmap= BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DataBaseManager.DB_PATH + "/" + DataBaseManager.CET4_DBNAME, null);
        Cursor cur = database.rawQuery("select id,word,marken,markus,translate from cet4 where id=id", null);
        int flag=0;
        int tmp_num=sp.getInt("SETTING_NUM",0);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    if (sp.getInt("CET4_POSITION",0)==flag){
                        word = cur.getString(cur.getColumnIndex("word"));
                        String tmp="";
                        String tmp_en="";
                        String tmp_us ="";
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
                        translate = tmp+cur.getString(cur.getColumnIndex("translate"));
                        listview_position=flag;
                    }
                    flag++;
                } while (cur.moveToNext());
            }
            cur.close();
            database.close();
        } else {
            cur.close();
            database.close();
        }
    }

    /**
     * 获得考研信息
     */
    private void GetTeefps() {
        bitmap= BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);
        SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(DataBaseManager.DB_PATH + "/" + DataBaseManager.TEEFPS_DBNAME, null);
        Cursor cur = database.rawQuery("select id,word,marken,markus,translate from teefps where id=id", null);
        int flag=0;
        int tmp_num=sp.getInt("SETTING_NUM",0);
        if (cur != null) {
            if (cur.moveToFirst()) {
                do {
                    if (sp.getInt("TEEFPS_POSITION",0)==flag){
                        word = cur.getString(cur.getColumnIndex("word"));
                        String tmp="";
                        String tmp_en="";
                        String tmp_us ="";
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
                        translate = tmp+cur.getString(cur.getColumnIndex("translate"));
                        listview_position=flag;
                    }
                    flag++;
                } while (cur.moveToNext());
            }
            cur.close();
            database.close();
        } else {
            cur.close();
            database.close();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
