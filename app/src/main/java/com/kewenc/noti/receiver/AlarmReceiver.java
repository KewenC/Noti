/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.SystemClock;
import com.kewenc.noti.service.NotificationService;

/**
 * Created by KewenC on 2016/12/31.
 */
public class AlarmReceiver extends BroadcastReceiver{
    private static final long TEN_MINUTE_Mill=300000;//定时时间间隔常量
    @Override
    public void onReceive(Context context, Intent intent) {
        if("com.kewenc.noti.action.repeating".equals(intent.getAction())){
            StartNotiService(context);
        }
    }

    /**
     * 定时开启NotificationService
     * @param context
     */
    private void StartNotiService(Context context) {
        Intent intent_repeating=new Intent(context, AlarmReceiver.class);
        intent_repeating.setAction("com.kewenc.noti.action.repeating");
        PendingIntent sender= PendingIntent.getBroadcast(context, 0, intent_repeating, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm=(AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            alarm.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+TEN_MINUTE_Mill,sender);
        }else if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.KITKAT){//api>=19
            alarm.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP,SystemClock.elapsedRealtime()+TEN_MINUTE_Mill,sender);
        }
        //to do something
        context.startService(new Intent(context,NotificationService.class));
    }
}
