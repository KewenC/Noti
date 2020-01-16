/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;
import androidx.appcompat.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.kewenc.noti.R;
import com.kewenc.noti.dao.Secret;
import java.util.Calendar;

public class ActivationActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et0;
    private Button btn00;
    private SharedPreferences sp;
    private Calendar calendar_r=Calendar.getInstance();//today
    private Calendar calendar_m=Calendar.getInstance();//yesterday
    private Calendar calendar_l=Calendar.getInstance();//the day before yesterday
    private int[] data={0,0,0};
    private View v0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activation);
        sp=getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
        final Toolbar toolbar=(Toolbar)findViewById(R.id.tb_rule);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        setSupportActionBar(toolbar);
        v0=findViewById(R.id.v0);
        et0=(EditText)findViewById(R.id.et0);
        btn00=(Button)findViewById(R.id.btn00);
        btn00.setOnClickListener(this);
        InitButton();
        calendar_r.setTimeInMillis(System.currentTimeMillis());
        calendar_m.setTimeInMillis(calendar_r.getTimeInMillis()-24*60*60*1000);
        calendar_l.setTimeInMillis(calendar_m.getTimeInMillis()-24*60*60*1000);
    }

    /**
     * 初始化Button和EditText
     */
    private void InitButton() {
        if(sp.getBoolean("NOTI_AD",false)){
            v0.setVisibility(View.GONE);
            et0.setVisibility(View.GONE);
            btn00.setText("已激活");
            btn00.setEnabled(false);
            ShowIMEI();
        }
    }

    /**
     * 展示手机IMEI号
     */
    private void ShowIMEI(){
        CardView cv_imei=(CardView)findViewById(R.id.cv_imei);
        cv_imei.setVisibility(View.VISIBLE);
        TextView tv_imei=(TextView)findViewById(R.id.tv_imei);
        tv_imei.setText(GetIMEI());
    }

    /**
     * 根据IMEI生成永久激活码
     * @return
     */
    private String GetIMEI(){
        TelephonyManager tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        int x4 = 0;
        int x3 = 1;
        int x2 = 2;
        int x1 = 3;
        String ID=tm.getDeviceId();//AdvertisingIdClient$Info#getId and for analytics, use InstanceId#getId.
        if (ID!=null){
            try {
                x4=Integer.parseInt(ID.substring(0,2));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                x3=Integer.parseInt(ID.substring(2,4));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                x2=Integer.parseInt(ID.substring(4,6));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            try {
                x1=Integer.parseInt(ID.substring(6,8));
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        Secret secret=new Secret();
        int IMEI=secret.GetData(0,x4,x3,x2,x1);
        return IMEI+"";
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        PackageInfo packageInfo;
        try {
            packageInfo = this.getPackageManager().getPackageInfo(
                    "com.taobao.taobao", 0);
        } catch (PackageManager.NameNotFoundException e) {
            packageInfo = null;
            e.printStackTrace();
        }
        int id=item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
            case R.id.tb_shop:
                Uri uri0=Uri.parse("https://item.taobao.com/item.htm?spm=0.0.0.0.phiZqy&id=546954859003");
                if (packageInfo!=null){
                    Intent intent0=new Intent();
                    intent0.setAction("Android.intent.action.VIEW");
                    intent0.setData(uri0);
                    intent0.setClassName("com.taobao.taobao","com.taobao.tao.detail.activity.DetailActivity");
                    startActivity(intent0);
                }else{
                    startActivity(new Intent(Intent.ACTION_VIEW,uri0));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tb_menu,menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        Secret secret=new Secret();
        switch (v.getId()){
            case R.id.btn00://激活引用四级码
                data[0]=secret.GetData(0
                        ,calendar_r.get(Calendar.YEAR)/100
                        ,calendar_r.get(Calendar.YEAR)%100
                        ,calendar_r.get(Calendar.MONTH)
                        ,calendar_r.get(Calendar.DAY_OF_MONTH));
                data[1]=secret.GetData(0
                        ,calendar_m.get(Calendar.YEAR)/100
                        ,calendar_m.get(Calendar.YEAR)%100
                        ,calendar_m.get(Calendar.MONTH)
                        ,calendar_m.get(Calendar.DAY_OF_MONTH));
                data[2]=secret.GetData(0
                        ,calendar_l.get(Calendar.YEAR)/100
                        ,calendar_l.get(Calendar.YEAR)%100
                        ,calendar_l.get(Calendar.MONTH)
                        ,calendar_l.get(Calendar.DAY_OF_MONTH));
                String editText0=et0.getText().toString();
                if (editText0.equals(""+data[0])||editText0.equals(""+data[1])
                        ||editText0.equals(""+data[2])||editText0.equals(GetIMEI())){
                    v0.setVisibility(View.GONE);
                    et0.setVisibility(View.GONE);
                    btn00.setText("已激活");
                    btn00.setEnabled(false);
                    SharedPreferences.Editor editor=sp.edit();
                    editor.putBoolean("NOTI_AD",true);
                    editor.apply();
                    ShowIMEI();
                    Snackbar.make(v,"激活成功！",Snackbar.LENGTH_SHORT).show();
                }else {
                    Snackbar.make(v,"激活码不正确！请重新输入！",Snackbar.LENGTH_SHORT).show();
                }
                break;
        }
//        Log.i("__DATA",data[0]+"\n"+data[1]+"\n"+data[2]);
    }
}
