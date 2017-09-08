/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.dao;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by KewenC on 2017/1/26.
 */

public class OverrideSimpleAdapter extends SimpleAdapter {
    /**
     * Constructor
     *
     * @param context  The context where the View associated with this SimpleAdapter is running
     * @param data     A List of Maps. Each entry in the List corresponds to one row in the list. The
     *                 Maps contain the data for each row, and should include all the entries specified in
     *                 "from"
     * @param resource Resource identifier of a view layout that defines the views for this list
     *                 item. The layout file should include at least those named views defined in "to"
     * @param from     A list of column names that will be added to the Map associated with each
     *                 item.
     * @param to       The views that should display column in the "from" parameter. These should all be
     *                 TextViews. The first N views in this list are given the values of the first N columns
     */

    private LayoutInflater mInflater;
    private ArrayList<Map<String, Object>> list;
    private int mResource;
    private int[] mTo;
    private String[] mFrom;

    public OverrideSimpleAdapter(Context context, ArrayList<Map<String, Object>> data, int resource, String[] from, int[] to) {
        super(context, data, resource, from, to);
        this.list=data;
        this.mInflater = LayoutInflater.from(context);
        this.mResource = resource;
        this.mFrom = from;
        this.mTo = to;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        // 判断是否缓存
        if (convertView == null) {
            holder = new ViewHolder();
            // 通过LayoutInflater实例化布局
            convertView = mInflater.inflate(mResource, null);
//            holder.img = (ImageView) convertView.findViewById(R.id.imageView);
            holder.num = (TextView) convertView.findViewById(mTo[0]);
            holder.word = (TextView) convertView.findViewById(mTo[1]);
            holder.translates = (TextView) convertView.findViewById(mTo[2]);
            convertView.setTag(holder);
        } else {
            // 通过tag找到缓存的布局
            holder = (ViewHolder) convertView.getTag();
        }
        // 设置布局中控件要显示的视图
//        holder.img.setBackgroundResource(R.drawable.ic_launcher);
        holder.num.setText(list.get(position).get(mFrom[0]).toString());
        holder.word.setText(list.get(position).get(mFrom[1]).toString());
        holder.translates.setText(list.get(position).get(mFrom[2]).toString());
        return convertView;
    }

    public final class ViewHolder {
//        public ImageView img;
        public TextView num;
        public TextView word;
        public TextView translates;
    }
}
