/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.dao;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ListView;

public class OverListView extends ListView {//自定义ListView
	private static final int MAX_Y_OVERSCROLL_DISTANCE = 200;
	private Context mContext;
	private int mMaxYOverscrollDistance;
	public OverListView(Context context) {
		super(context);
		mContext = context;
		initBounceListView();
	}
	public OverListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context;
		initBounceListView();
	}
	public OverListView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		mContext = context;
		initBounceListView();
	}


	private void initBounceListView(){
		//get the density of the screen and do some maths with it on the max overscroll distance
		//variable so that you get similar behaviors no matter what the screen size

		final DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
		final float density = metrics.density;
		mMaxYOverscrollDistance =(int)(density * MAX_Y_OVERSCROLL_DISTANCE);
	}
	@Override
	protected boolean overScrollBy(int deltaX, int deltaY, int scrollX, int scrollY, int scrollRangeX, int scrollRangeY,
								   int maxOverScrollX, int maxOverScrollY, boolean isTouchEvent) {
		// TODO Auto-generated method stub
		return super.overScrollBy(deltaX, deltaY, scrollX, scrollY, scrollRangeX, scrollRangeY, maxOverScrollX, mMaxYOverscrollDistance,
				isTouchEvent);
	}

}
