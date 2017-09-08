/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.Fragment;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import com.kewenc.noti.R;
import com.kewenc.noti.activity.SingleActivity;
import com.kewenc.noti.dao.ListData;
import com.kewenc.noti.dao.ListViewOnItemLongClickListener;
import com.kewenc.noti.dao.OverListView;
import com.kewenc.noti.dao.OverOnTouchListener;
import com.kewenc.noti.dao.OverrideSimpleAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class Cet6Fragment extends Fragment {
    private OverListView lv;
    private LoadData loadData;
    public Cet6Fragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_common, container, false);
        TextView tv_title_common=(TextView)view.findViewById(R.id.tv_title_common);
        tv_title_common.setText("六级");
        lv=(OverListView)view.findViewById(R.id.lv);
        lv.setEmptyView(view.findViewById(R.id.tv_loading));
        loadData=new LoadData();
        loadData.execute(1);
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
    private class LoadData extends AsyncTask<Integer,Void,OverrideSimpleAdapter> {

        @Override
        protected OverrideSimpleAdapter doInBackground(Integer... params) {
            return ListData.DoAdapter(getActivity(),params[0]);
        }

        @Override
        protected void onPostExecute(OverrideSimpleAdapter overrideSimpleAdapter) {
            SharedPreferences sp=getContext().getSharedPreferences("NOTI_DATA", Activity.MODE_PRIVATE);
            lv.setAdapter(overrideSimpleAdapter);
            lv.setSelection(sp.getInt("CET6_POSITION",0));
            lv.setOnItemClickListener(new LvOnItemClickListener());
            lv.setOnItemLongClickListener(new ListViewOnItemLongClickListener(getContext(),1));
            lv.setOnTouchListener(new OverOnTouchListener(getContext()));
        }
    }
    /**
     * listview的监听器
     */
    private class LvOnItemClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Intent intent=new Intent(new Intent(getActivity(), SingleActivity.class));
            Bundle bundle=new Bundle();
            bundle.putInt("FLAG",1);
            bundle.putInt("POSITION",position);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
}
