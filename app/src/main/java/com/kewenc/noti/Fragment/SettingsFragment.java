/*
 * Copyright (c) 2016.
 * KewenC 版权所有
 */

package com.kewenc.noti.Fragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.kewenc.noti.R;
import com.kewenc.noti.activity.ActivationActivity;
import com.kewenc.noti.activity.UseActivity;
import com.kewenc.noti.receiver.AlarmReceiver;

import static android.content.Context.NOTIFICATION_SERVICE;
import static android.util.TypedValue.COMPLEX_UNIT_SP;


/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends Fragment implements View.OnClickListener{
    private RelativeLayout rl_num;
    private TextView tv_num,tv_grade,tv_share,tv_about, tv_clear_noti;
    private SharedPreferences sp;
    private AlertDialog.Builder mBuilder;
    private String num[]={"只显示英式音标","只显示美式音标","同时显示英美式","不显示音标"};
    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        sp=getActivity().getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
        rl_num=(RelativeLayout)view.findViewById(R.id.rl_num);
        tv_num=(TextView)view.findViewById(R.id.tv_num);
        view.findViewById(R.id.tv_activation).setOnClickListener(this);
        tv_grade=(TextView)view.findViewById(R.id.tv_grade);
        tv_share=(TextView)view.findViewById(R.id.tv_share);
        tv_about=(TextView)view.findViewById(R.id.tv_about);
        tv_clear_noti=(TextView)view.findViewById(R.id.tv_clear_noti);
        tv_num.setText(num[sp.getInt("SETTING_NUM",2)]);//音标选取初始化
        tv_grade.setOnClickListener(this);
        rl_num.setOnClickListener(this);
        tv_share.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        tv_clear_noti.setOnClickListener(this);
        return view;
//        Log.i("_NotiF","b");
////        TelephonyManager tm = (TelephonyManager) getContext().getSystemService(TELEPHONY_SERVICE);
////        Log.i("IMEI", tm.getDeviceId());
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.tv_grade:
                Grade();
                break;
            case R.id.rl_num:
                SoundMark();
                break;
            case R.id.tv_activation:
                Activation();
                break;
            case R.id.tv_share:
                Share();
                break;
            case R.id.tv_about:
                About();
                break;
            case R.id.tv_clear_noti:
                ClearNoti();
                break;
        }
    }

    /**
     * 清除通知栏
     */
    private void ClearNoti() {
        NotificationManager nm = (NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
        if (nm != null){
            nm.cancelAll();
        }
        AlarmManager alarm=(AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        if(alarm!=null) {//取消闹铃
            Intent intent=new Intent(getContext(), AlarmReceiver.class);
            intent.setAction("com.kewenc.noti.action.repeating");
            PendingIntent sender=PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            alarm.cancel(sender);
        }
        Toast.makeText(getContext(),"通知栏单词已清除！",Toast.LENGTH_SHORT).show();
    }

    /**
     * 打开激活Activity
     */
    private void Activation() {
        startActivity(new Intent(getActivity(),ActivationActivity.class));
    }

    /**
     * 关于应用
     */
    private void About() {
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getContext());
        final LinearLayout linearLayout=new LinearLayout(getContext());
        final TextView tv = new TextView(getContext());
        final TextView tv1 = new TextView(getContext());
        tv.setText("\n"+"\n"+"\n"+"通知栏背单词"+"\n"+"版本1.2"+"\n");
        tv1.setText("Copyright (c) 2016-2017."+"\n"+"KewenC 版权所有");
        tv.setTextSize(COMPLEX_UNIT_SP,16);
        tv1.setTextSize(COMPLEX_UNIT_SP,14);
        tv.setGravity(Gravity.CENTER);
        tv1.setGravity(Gravity.CENTER);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(tv, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayout.addView(tv1,ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        builder.setView(linearLayout)
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0,
                                                int arg1) {

                            }
                        })
                .create().show();
    }

    /**
     * 常见问题
     */
    private void Share() {
        startActivity(new Intent(getContext(), UseActivity.class));
    }

    /**
     * 音标选取
     */
    private void SoundMark() {
        mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("音标选取：");
        mBuilder.setItems(num,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        tv_num.setText(num[which]);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putInt("SETTING_NUM", which);
                        editor.commit();
                    }
                });
        mBuilder.create().show();
    }

    /**
     * 给应用评分
     */
    private void Grade() {
        Uri uri = Uri.parse("market://details?id="+getActivity().getPackageName());
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
