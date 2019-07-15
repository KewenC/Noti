/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.activity;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.kewenc.noti.Fragment.Cet4Fragment;
import com.kewenc.noti.Fragment.Cet6Fragment;
import com.kewenc.noti.Fragment.IeltsFragment;
import com.kewenc.noti.Fragment.NotiFragment;
import com.kewenc.noti.Fragment.TeefpsFragment;
import com.kewenc.noti.R;
import com.kewenc.noti.dao.DataBaseHelper;
import com.kewenc.noti.dao.DataBaseManager;
import com.kewenc.noti.Fragment.MeFragment;
import com.kewenc.noti.Fragment.SettingsFragment;
import com.kewenc.noti.dao.DataBaseOpenHelper;
import com.tencent.stat.StatService;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    private FragmentTransaction fragmentTransaction;
    private static RelativeLayout cv_main;//导航
    private static Boolean isExit = false;
    private static final int[] icon = {R.drawable.nav_noti,R.drawable.nav_ciku,R.drawable.nav_me,R.drawable.nav_setting};
    private static final String[] FLAG = {"四级","六级","考研","雅思"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme);//启动页后，回归主题
        setContentView(R.layout.activity_main);
//        //打开debug,可查看mta上报日志或错误
//        StatConfig.setDebugEnable(true);
        //调用统计接口，触发MTA并上报数据
        StatService.trackCustomEvent(this, "onCreate", "");
        //初始化加载数据库



//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                DataBaseManager dataBaseManager=new DataBaseManager(getApplicationContext());
//                dataBaseManager.openNativeDatabase();
////                dataBaseManager.openCet4Database();
////                dataBaseManager.openCet6Database();
////                dataBaseManager.openTeefpsDatabase();
////                dataBaseManager.openIeltsDatabase();
////                DataBaseOpenHelper dataBaseOpenHelper = new DataBaseOpenHelper(MainActivity.this);
////                dataBaseOpenHelper.getWritableDatabase();
//            }
//        }).start();
        cv_main = findViewById(R.id.cv_main);
        BottomNavigationBar btnBar = findViewById(R.id.bomnbar);
        SelectNavigation(0);//初始化导航栏
        btnBar  .addItem(new BottomNavigationItem(icon[0],"通知").setActiveColor(R.color.colorPrimaryDark))
                .addItem(new BottomNavigationItem(icon[1],"词库").setActiveColor(R.color.color_green))
                .addItem(new BottomNavigationItem(icon[2],"我的").setActiveColor(R.color.color_blue))
                .addItem(new BottomNavigationItem(icon[3],"设置").setActiveColor(R.color.color_gray))
                .setMode(BottomNavigationBar.MODE_CLASSIC)
                .setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC)
                .initialise();
        btnBar.setTabSelectedListener(new BNBARTabSelectedListener());
        findViewById(R.id.rlSearch).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });
    }

    /**
     * 导航栏选择
     */
    private void SelectNavigation(int position) {
        final FragmentManager fragmentManager=getSupportFragmentManager();
        switch (position){
            case 0:
                NotiFragment notiFragment = new NotiFragment();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,notiFragment);
                fragmentTransaction.commit();
                break;
            case 1:
                SharedPreferences sp=getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
                int access_flag=sp.getInt("ACCESS_FLAG",0)==4?0:sp.getInt("ACCESS_FLAG",0);
                Change(access_flag);
                break;
            case 2:
                MeFragment meFragment=new MeFragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,meFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                SettingsFragment settingsFragment=new SettingsFragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,settingsFragment);
                fragmentTransaction.commit();
                break;
        }

    }

    /**
     * 导航栏的监听器
     */
    private class BNBARTabSelectedListener implements BottomNavigationBar.OnTabSelectedListener {
        @Override
        public void onTabSelected(int position) {//当Item被选中状态
            SelectNavigation(position);
        }

        @Override
        public void onTabUnselected(int position) {//当Item不被选中状态

        }

        @Override
        public void onTabReselected(int position) {// 当Item再次被选中状态
            if(position==2){//我的
                Intent intent=new Intent(MainActivity.this,CreateActivity.class);
                startActivity(intent);
            }
            if(position==1){//词库
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(MainActivity.this);
                mBuilder.setTitle("词库选取：");
                mBuilder.setItems(FLAG,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Change(which);
                            }
                        });
                mBuilder.create().show();
            }
        }
    }

    /**
     * 选择四词库
     * @param which
     */
    private void Change(int which) {
        FragmentTransaction fragmentTransaction;
        FragmentManager fragmentManager=getSupportFragmentManager();
        switch (which){
            case 0:
                Cet4Fragment cet4Fragment=new Cet4Fragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,cet4Fragment);
                fragmentTransaction.commit();
                break;
            case 1:
                Cet6Fragment cet6Fragment=new Cet6Fragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,cet6Fragment);
                fragmentTransaction.commit();
                break;
            case 2:
                TeefpsFragment teefpsFragment=new TeefpsFragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,teefpsFragment);
                fragmentTransaction.commit();
                break;
            case 3:
                IeltsFragment ieltsFragment=new IeltsFragment();
                fragmentTransaction=fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.container,ieltsFragment);
                fragmentTransaction.commit();
                break;
        }
    }
    /**
     * 导航栏显示隐藏切换
     * @param flag
     */
    public static void isShow(int flag){
        Animator mAnimator = null;
        if(mAnimator!=null&&mAnimator.isRunning()){
            mAnimator.cancel();
        }
        if (flag==0){
            mAnimator= ObjectAnimator.ofFloat(cv_main,"translationY",cv_main.getTranslationY(),0);
        }else{
            mAnimator=ObjectAnimator.ofFloat(cv_main, "translationY", cv_main.getTranslationY(),2*cv_main.getHeight());
        }
        mAnimator.setDuration(300);
        mAnimator.start();
    }

    /**
     * 手机返回键操作
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK)
        {
            exitBy2Click();//调用双击退出函数
        }
        return false;
    }

    /**
     * 双击退出函数
     */
    private void exitBy2Click() {
        Timer tExit=null;
        if(isExit==false)
        {
            isExit=true;//准备退出
            Toast.makeText(this,getResources().getString(R.string.exit_program_main), Toast.LENGTH_SHORT).show();
            tExit=new Timer();
            tExit.schedule(new TimerTask(){

                @Override
                public void run() {
                    isExit=false;//取消退出
                }
            },2000);//如果2秒中内没有按下返回键，则启动定时器取消刚才执行任务
        }else
        {
            finish();
            System.exit(0);
        }
    }
}
