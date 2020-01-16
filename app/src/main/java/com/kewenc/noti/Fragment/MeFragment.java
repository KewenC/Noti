/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.Fragment;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.TextView;
import com.kewenc.noti.R;
import com.kewenc.noti.activity.AmendActivity;
import com.kewenc.noti.activity.SingleActivity;
import com.kewenc.noti.dao.ListData;
import com.kewenc.noti.dao.MeDAO;
import com.kewenc.noti.dao.NotiDao;
import com.kewenc.noti.dao.OverListView;
import com.kewenc.noti.dao.OverOnTouchListener;
import com.kewenc.noti.dao.OverrideSimpleAdapter;
import com.kewenc.noti.model.Tb_me;
import com.kewenc.noti.service.NotificationService;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends Fragment {
    private static OverListView lv_me;
    private static ArrayList<Map<String, Object>> list;
    private static OverrideSimpleAdapter simpleAdapter;
    private static final int REQUEST=0;
    private static final String menu[]={"修改","删除","跳跃通知进度在这"};
    private static int index_max;
    private static SharedPreferences sp;
    private TextView textView;
    private LoadData loadData;
    public MeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        sp=getContext().getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
        list = new ArrayList<Map<String, Object>>();
        lv_me=(OverListView)view.findViewById(R.id.lv_me);
        textView= (TextView) view.findViewById(R.id.tv_empty);
        NotiDao notiDao = new NotiDao(getContext());
        if (notiDao.getCount()==0){
            textView.setText("再次点击导航栏“我的”\n创建专属的词库吧！");
        }else {
            if (notiDao.getCount()>99){//数据库条数达到100个显示加载提示
                textView.setText("加载中..");
            }
        }
//        MeDAO meDAO=new MeDAO(getContext());
//        if (meDAO.getCount()==0){
//            textView.setText("再次点击导航栏“我的”\n创建专属的词库吧！");
//        }else {
//            if (meDAO.getCount()>99){//数据库条数达到100个显示加载提示
//                textView.setText("加载中..");
//            }
//        }
        lv_me.setEmptyView(textView);
        loadData=new LoadData();
        loadData.execute(4);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                lv_me.post(new Runnable() {
//                    public void run() {
//                        simpleAdapter=ListData.DoAdapter(getContext(),4);
//                        lv_me.setAdapter(simpleAdapter);
//                        list=ListData.list;
//                        index_max=list.size();
//                        lv_me.setSelection(sp.getInt("ME_POSITION",0));
//                        lv_me.setOnItemClickListener(new LvOnItemClickListener());
//                        lv_me.setOnItemLongClickListener(new LvOnItemLongClickListener());
//                        lv_me.setOnTouchListener(new OverOnTouchListener(getContext()));
//                    }
//                });
//            }
//        }).start();
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
    private class LoadData extends AsyncTask<Integer,Void,OverrideSimpleAdapter>{

        @Override
        protected OverrideSimpleAdapter doInBackground(Integer... params) {
            simpleAdapter=ListData.DoAdapter(getContext(),params[0]);
            return simpleAdapter;
        }

        @Override
        protected void onPostExecute(OverrideSimpleAdapter overrideSimpleAdapter) {
            lv_me.setAdapter(overrideSimpleAdapter);
            list=ListData.list;
            index_max=list.size();
            lv_me.setSelection(sp.getInt("ME_POSITION",0));
            lv_me.setOnItemClickListener(new LvOnItemClickListener());
            lv_me.setOnItemLongClickListener(new LvOnItemLongClickListener());
            lv_me.setOnTouchListener(new OverOnTouchListener(getContext()));
        }
    }
    /**
     * 仅供外部调用，添加数据刷新adapter
     * @param word
     * @param translate
     */
    public static void AddData(String word,String marken,String markus,String translate){
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("num",++index_max);
        map.put("word",word);
        int tmp_num=sp.getInt("SETTING_NUM",0);
        String tmp="";
        switch (tmp_num){
            case 0:
                markus="";
                break;
            case 1:
                marken="";
                break;
            case 2:
                break;
            case 3:
                markus="";
                marken="";
                break;
        }
        if (!marken.equals("")){
            if (!markus.equals("")){
                tmp="英 "+marken+"  美 "+markus+"\n";
            }else{
                tmp="英 "+marken+"\n";
            }
        }else{
            if (!markus.equals("")){
                tmp="美 "+markus+"\n";
            }else{

            }
        }
        map.put("translates",tmp+translate);
        Log.i("_",""+list.size());
        list.add(list.size(),map);
        simpleAdapter.notifyDataSetChanged();
        lv_me.setSelection(list.size());
    }

    /**
     * ListView 点击监听器
     */
    private class LvOnItemClickListener implements android.widget.AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(new Intent(getActivity(), SingleActivity.class));
            Bundle bundle=new Bundle();
            bundle.putInt("FLAG",4);
            bundle.putInt("POSITION",position);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    /**
     * ListView 长按监听器
     */
    private class LvOnItemLongClickListener implements android.widget.AdapterView.OnItemLongClickListener {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
            final int mposition=position;
            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
//            mBuilder.setTitle("选择显示单词的个数：");
            mBuilder.setItems(menu,
                    new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MeDAO meDAO=new MeDAO(getActivity());
                            int ID=ListData.array_id[position];
                            if (which==0){//修改
                                Intent intent=new Intent(getActivity(), AmendActivity.class);
                                Bundle bundle=new Bundle();
                                bundle.putInt("POSITION",mposition);
                                bundle.putString("WORD",list.get(mposition).get("word").toString());
                                bundle.putInt("ID",ID);
                                intent.putExtras(bundle);
                                startActivityForResult(intent,REQUEST);
                            }else if(which==1){//删除
                                meDAO.detele(ID);
                                list.remove(mposition);
                                simpleAdapter.notifyDataSetChanged();
                                ListData.DoAdapter(getContext(),4);//刷新id数组的数据
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("ME_POSITION",(sp.getInt("ME_POSITION",0)-1)>-1?(sp.getInt("ME_POSITION",0)-1):0);//及时更新索引号
                                editor.apply();
                                if (meDAO.getCount()==0){//没有数据时初始化回归
                                    textView.setText("再次点击导航栏“我的”\n创建专属的词库吧！");
                                }
                            }else {
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putInt("ACCESS_FLAG", 4);
                                editor.putInt("ME_POSITION",position);//待处理。。。
                                editor.putInt("TOTI_WORD_NUMBER",sp.getInt("TOTI_WORD_NUMBER",0)+1);
                                editor.apply();
                                getContext().startService(new Intent(getContext(), NotificationService.class));
                            }
                        }
                    });
            mBuilder.create().show();
            return false;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode==-1){
            Bundle bundle=data.getExtras();
            int position=bundle.getInt("POSITION");
            int id=bundle.getInt("ID");
            String word=bundle.getString("WORD");
            String marken=bundle.getString("MARKEN");
            String markus=bundle.getString("MARKUS");
            String translate=bundle.getString("TRANSLATE");
            Map<String,Object> map = new HashMap<String,Object>();
            map.put("num",position+1);
            map.put("word",word);
            int tmp_num=sp.getInt("SETTING_NUM",0);
            String tmp="";
            switch (tmp_num){
                case 0:
                    markus="";
                    break;
                case 1:
                    marken="";
                    break;
                case 2:
                    break;
                case 3:
                    markus="";
                    marken="";
                    break;
            }
            if (!marken.equals("")){
                if (!markus.equals("")){
                    tmp="英 "+marken+"  美 "+markus+"\n";
                }else{
                    tmp="英 "+marken+"\n";
                }
            }else{
                if (!markus.equals("")){
                    tmp="美 "+markus+"\n";
                }else{

                }
            }
            map.put("translates",tmp+translate);
            list.set(position,map);
            simpleAdapter.notifyDataSetChanged();
            MeDAO meDAO=new MeDAO(getActivity());
            Tb_me tb_me=new Tb_me(id,word,marken,markus,translate);
            meDAO.update(tb_me);
        }
    }
}
