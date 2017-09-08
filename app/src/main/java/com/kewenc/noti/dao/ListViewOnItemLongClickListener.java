/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.dao;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.kewenc.noti.activity.AmendActivity;
import com.kewenc.noti.service.NotificationService;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by KewenC on 2017/3/7.
 */

public class ListViewOnItemLongClickListener implements AdapterView.OnItemLongClickListener{
    private int flag;
    private Context context;
    private SharedPreferences sp;
    private final static String[] SP_ACCESS_POSITION={"CET4_POSITION","CET6_POSITION","TEEFPS_POSITION","IELTS_POSITION"};
    public ListViewOnItemLongClickListener(Context context,int flag) {
        super();
        this.context=context;
        this.flag=flag;
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(context);
        builder.setTitle("跳跃通知进度在这？")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {
                                sp=context.getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("ACCESS_FLAG", flag);
                                editor.putInt(SP_ACCESS_POSITION[flag],position);//待处理。。。
                                editor.putInt("TOTI_WORD_NUMBER",sp.getInt("TOTI_WORD_NUMBER",0)+1);
                                editor.commit();
                                context.startService(new Intent(context, NotificationService.class));
                            }
                        })
                .setNegativeButton("取消",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                .create().show();
        return false;
    }
}
