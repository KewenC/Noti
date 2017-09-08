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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.kewenc.noti.R;
import com.kewenc.noti.dao.MeDAO;
import com.kewenc.noti.receiver.AlarmReceiver;

public class AssistService extends Service {
    private SharedPreferences sp;
    private boolean isLast=true;
    public AssistService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sp=this.getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        int flag=sp.getInt("ACCESS_FLAG",0);
        switch (flag){
            case 0:
                int cet4=sp.getInt("CET4_POSITION",0)+1;
                if (cet4<4590){
                    editor.putInt("CET4_POSITION",cet4);
                }else{
                    isLast=false;
                }
                break;
            case 1:
                int cet6=sp.getInt("CET6_POSITION",0)+1;
                if (cet6<2089){
                    editor.putInt("CET6_POSITION",cet6);
                }else{
                    isLast=false;
                }
                break;
            case 2:
                int teefps=sp.getInt("TEEFPS_POSITION",0)+1;
                if (teefps<5492){
                    editor.putInt("TEEFPS_POSITION",teefps);
                }else{
                    isLast=false;
                }
                break;
            case 3:
                int ielts=sp.getInt("IELTS_POSITION",0)+1;
                if (ielts<7930){
                    editor.putInt("IELTS_POSITION",ielts);
                }else{
                    isLast=false;
                }
                break;
            case 4:
                int me=sp.getInt("ME_POSITION",0)+1;
                MeDAO meDAO=new MeDAO(getApplicationContext());
                if (me<meDAO.getCount()){
                    editor.putInt("ME_POSITION",me);
                }else{
                    isLast=false;
                }
                break;
        }
        if (isLast){
            editor.putInt("TOTI_WORD_NUMBER",sp.getInt("TOTI_WORD_NUMBER",0)+1);
            Intent intent1=new Intent(this,NotificationService.class);
            startService(intent1);
        }else {
            LastNoti();
        }
        editor.commit();
        stopSelf();
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 开启背诵完成通知
     */
    private void LastNoti() {
        Bitmap bitmap= BitmapFactory.decodeResource(this.getResources(),R.mipmap.ic_launcher);
        NotificationCompat.Builder builder=new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ticket)
                .setLargeIcon(bitmap)
                .setPriority(NotificationCompat.PRIORITY_MAX);//通知优先级;
        builder.setContentTitle("恭喜您，背诵完成！").setContentText("请选择其他词库或重新开始吧！");
        Notification notification=builder.build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        NotificationManager nm=(NotificationManager)this.getSystemService(NOTIFICATION_SERVICE);
        nm.notify(5,notification);
        AlarmManager alarm=(AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        if(alarm!=null) {//取消闹铃
            Intent intent=new Intent(this, AlarmReceiver.class);
            intent.setAction("com.kewenc.noti.action.repeating");
            PendingIntent sender=PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm.cancel(sender);
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
