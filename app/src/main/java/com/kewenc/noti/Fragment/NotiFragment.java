/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.kewenc.noti.R;
import com.kewenc.noti.dao.NotiDao;
import com.kewenc.noti.dao.OverListView;
import com.kewenc.noti.dao.OverOnTouchListener;
import com.kewenc.noti.service.NotificationService;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotiFragment extends Fragment implements View.OnClickListener{
    private OverListView lv_noti;
    private LoadData loadData;
    private String[] flag={"四级进度：","六级进度：","考研进度：","雅思进度：","我的进度："};
    private String[] mFlag={"四级","六级","考研","雅思","我的"};
    private SharedPreferences sp;
    private DecimalFormat df=new DecimalFormat("######0.00");
    private TextView tv_flag;
    //AD
    private ViewGroup bannerContainer;
    private BannerView bv;
    public static final String APPID = "1105794797";
    public static final String BannerPosID = "8060425192692594";
    //AD
    public NotiFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_noti, container, false);
        getContext().startService(new Intent(getContext(),NotificationService.class));//打开默认通知单词
        sp = getContext().getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
        if (!sp.getBoolean("NOTI_AD",false)){
            //AD
            bannerContainer = (ViewGroup)view.findViewById(R.id.adcontent);
            this.initBanner();
            this.bv.loadAD();
            //AD
        }
        tv_flag=(TextView)view.findViewById(R.id.tv_impress);//正在通知词库
        tv_flag.setText(mFlag[sp.getInt("ACCESS_FLAG",0)]);
//        Button btn_cleanAll=(Button)view.findViewById(R.id.btn_cleanall);//清除通知
//        btn_cleanAll.setOnClickListener(this);
        lv_noti = view.findViewById(R.id.lv_noti);
        loadData = new LoadData();
        loadData.execute();
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        loadData.cancel(true);
    }

    /**
     * 异步任务类，加载数据库
     */
    private class LoadData extends AsyncTask<Void,Void,SimpleAdapter> {

        @Override
        protected SimpleAdapter doInBackground(Void... params) {
            return new SimpleAdapter(getContext(),GetData(),R.layout.listview_noti,new String[]{"flag","left","right"},
                    new int[]{R.id.tv_flag,R.id.tv_left,R.id.tv_right});
        }

        @Override
        protected void onPostExecute(SimpleAdapter simpleAdapter) {
            lv_noti.setAdapter(simpleAdapter);
            lv_noti.setOnItemClickListener(new LvOnItemClickListener());
            lv_noti.setOnTouchListener(new OverOnTouchListener(getContext()));
        }
    }

    /**
     * 获取数据
     * @return
     */
    private ArrayList<Map<String, Object>> GetData() {
        ArrayList<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        Map<String, Object> map;
        for (int i=0;i<5;i++){
            map = new HashMap<String, Object>();
            map.put("flag", flag[i]);
            switch (i){
                case 0:
                    map.put("left", (sp.getInt("CET4_POSITION",0)+1)+"/4590");
                    map.put("right", df.format((sp.getInt("CET4_POSITION",0)+1)*100.0/4590)+"%");
                    break;
                case 1:
                    map.put("left", (sp.getInt("CET6_POSITION",0)+1)+"/2089");
                    map.put("right", df.format((sp.getInt("CET6_POSITION",0)+1)*100.0/2089)+"%");
                    break;
                case 2:
                    map.put("left", (sp.getInt("TEEFPS_POSITION",0)+1)+"/5492");
                    map.put("right", df.format((sp.getInt("TEEFPS_POSITION",0)+1)*100.0/5492)+"%");
                    break;
                case 3:
                    map.put("left", (sp.getInt("IELTS_POSITION",0)+1)+"/7930");
                    map.put("right", df.format((sp.getInt("IELTS_POSITION",0)+1)*100.0/7930)+"%");
                    break;
                case 4:
//                    MeDAO meDAO=new MeDAO(getContext());
//                    int count = (int)meDAO.getCount();
                    NotiDao notiDao = new NotiDao(getContext());
                    int count = (int) notiDao.getCount();
                    if (count!=0){
                        map.put("left", (sp.getInt("ME_POSITION",0)+1)+"/"+count);
                        map.put("right", df.format((sp.getInt("ME_POSITION",0)+1)*100.0/count)+"%");
                    }else{
                        map.put("left", "0/0");
                        map.put("right", "0.00%");
                    }
                    break;
            }
            list.add(map);
        }
        return list;
    }

    @Override
    public void onClick(View v) {
//        int id=v.getId();
//        switch (id){
//            case R.id.btn_cleanall://清除通知按钮
//                NotificationManager nm=(NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
//                nm.cancelAll();
//                AlarmManager alarm=(AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
//                if(alarm!=null) {//取消闹铃
//                    Intent intent=new Intent(getContext(), AlarmReceiver.class);
//                    intent.setAction("com.kewenc.noti.action.repeating");
//                    PendingIntent sender=PendingIntent.getBroadcast(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    alarm.cancel(sender);
//                }
//                Toast.makeText(getContext(),"通知栏单词已清除！",Toast.LENGTH_SHORT).show();
//
////                expandNotification(getActivity());
//                break;
//        }
    }

//    /**
//     * 折叠通知栏
//     *
//     * @param context
//     */
//    public static void collapsingNotification(Context context) {
//        Object service = context.getSystemService("statusbar");
//        if (null == service)
//            return;
//        try {
//            Class<?> clazz = Class.forName("android.app.StatusBarManager");
//            int sdkVersion = android.os.Build.VERSION.SDK_INT;
//            Method collapse = null;
//            if (sdkVersion <= 16) {
//                collapse = clazz.getDeclaredMethod("collapse");
//            } else {
//                collapse = clazz.getDeclaredMethod("collapsePanels");
//            }
//            collapse.setAccessible(true);
//            collapse.invoke(service);
//        } catch (Exception e) {
////       //e.printStackTrace();
//        }
//    }
//
//    /**
//     * 展开通知栏
//     * @param context
//     */
//    public static void expandNotification(Context context) {
//        Object service = context.getSystemService("statusbar");
//        if (null == service)
//            return;
//        try {
//            Class<?> clazz = Class.forName("android.app.StatusBarManager");
//            int sdkVersion = android.os.Build.VERSION.SDK_INT;
//            Method expand = null;
//            if (sdkVersion <= 16) {
//                expand = clazz.getDeclaredMethod("expand");
//            } else {
//                /*
//                 * Android SDK 16之后的版本展开通知栏有两个接口可以处理
//                 * expandNotificationsPanel()
//                 * expandSettingsPanel()
//                 */
//                //expand =clazz.getMethod("expandNotificationsPanel");
//                expand = clazz.getDeclaredMethod("expandSettingsPanel");
//            }
//            expand.setAccessible(true);
//            expand.invoke(service);
//        } catch (Exception e) {
////       //e.printStackTrace();
//        }
//    }

    private class LvOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
            AlertDialog.Builder builder =
                    new AlertDialog.Builder(getContext());
            builder.setTitle("设为当前通知词库?")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface arg0,
                                                    int arg1) {
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putInt("ACCESS_FLAG", position);
                                    tv_flag.setText(mFlag[position]);
                                    editor.apply();
                                    getContext().startService(new Intent(getContext(),NotificationService.class));

                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    NotificationManager nm=(NotificationManager)getContext().getSystemService(NOTIFICATION_SERVICE);
                                    nm.cancelAll();
                                }
                            })
                    .create().show();

        }
    }

    /**
     * banner广告函数
     */
    private void initBanner() {
        this.bv=new BannerView(getActivity(), ADSize.BANNER, APPID, BannerPosID);
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
