/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.kewenc.noti.R;
import com.kewenc.noti.dao.ListData;
import com.kewenc.noti.dao.MeDAO;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

public class SingleActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView tv_title_single,tv_word_single,tv_mark_single,tv_translates_single,tv_sort;
    private Button btn_last,btn_next;
    private int position;
    private static final int[] total={4590,2089,5492,7930};
    private int flag;
    //AD
    private ViewGroup bannerContainer;
    private BannerView bv;
    public static final String APPID = "1105794797";
    public static final String BannerPosID = "8060425192692594";
    //AD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        Toolbar toolbar=(Toolbar)findViewById(R.id.tb_single);
        toolbar.setNavigationIcon(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        toolbar.setTitle("");
        SharedPreferences sp=getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
        if (sp.getBoolean("NOTI_AD",false)){
        }else {
            //AD
            bannerContainer = (ViewGroup)findViewById(R.id.adcontent);
            this.initBanner();
            this.bv.loadAD();
            //AD
        }
        setSupportActionBar(toolbar);
        tv_title_single=(TextView)findViewById(R.id.tv_title_single);
        tv_word_single=(TextView)findViewById(R.id.tv_word_single);
        tv_mark_single=(TextView)findViewById(R.id.tv_mark_single);
        tv_translates_single=(TextView)findViewById(R.id.tv_translates_single);
        tv_sort=(TextView)findViewById(R.id.tv_sort);
        btn_last=(Button)findViewById(R.id.btn_last);
        btn_next=(Button)findViewById(R.id.btn_next);
        btn_last.setOnClickListener(this);
        btn_next.setOnClickListener(this);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        flag=bundle.getInt("FLAG");
        switch (flag){
            case 0:
                tv_title_single.setText("四级");
                ListData.DoAdapter(getApplication(),0);//刷新
                break;
            case 1:
                tv_title_single.setText("六级");
                ListData.DoAdapter(getApplication(),1);//刷新
                break;
            case 2:
                tv_title_single.setText("考研");
                ListData.DoAdapter(getApplication(),2);//刷新
                break;
            case 3:
                tv_title_single.setText("雅思");
                ListData.DoAdapter(getApplication(),3);//刷新
                break;
            case 4:
                tv_title_single.setText("我的");
                ListData.DoAdapter(getApplication(),4);//刷新
                break;
        }
        position=bundle.getInt("POSITION");
        UpdateWord(position,findViewById(R.id.activity_single));
        Where(position);
    }

    private void Where(int position){
        ++position;
        if (flag!=4){
            tv_sort.setText(position+"/"+total[flag]);
        }else {
            MeDAO meDAO=new MeDAO(this);
            tv_sort.setText(position+"/"+meDAO.getCount());
        }
    }
    /**
     * Called when a view has been clicked.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_last:
                UpdateWord(--position,v);
                break;
            case R.id.btn_next:
                UpdateWord(++position,v);
                break;
        }
    }

    /**
     * 更新数据
     * @param i
     */
    private void UpdateWord(int i, View v) {
        tv_word_single.setText(ListData.array_word[i]);
        tv_mark_single.setText(ListData.array_mark[i]);
        tv_translates_single.setText(ListData.array_translates[i]);
        if (i-1>-1){
            btn_last.setText(ListData.array_word[i-1]);
            btn_last.setVisibility(View.VISIBLE);
        }else{
            btn_last.setVisibility(View.GONE);
        }
        if (i+1<ListData.array_word.length){
            btn_next.setText(ListData.array_word[i+1]);
            btn_next.setVisibility(View.VISIBLE);
        }else {
            Snackbar.make(v,"最后一个啦！",Snackbar.LENGTH_LONG).show();
            btn_next.setVisibility(View.GONE);
        }
        Where(i);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    /**
     * banner广告函数
     */
    private void initBanner() {
        this.bv=new BannerView(this, ADSize.BANNER, APPID, BannerPosID);
        bv.setRefresh(15);
        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(int arg0) {
                Log.i("AD_DEMO", "BannerNoAD_Code=" + arg0);
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }
        });
        bannerContainer.addView(bv);
    }
}
