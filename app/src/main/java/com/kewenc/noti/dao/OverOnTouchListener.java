/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.dao;

import android.animation.Animator;
import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import com.kewenc.noti.activity.MainActivity;

/**
 * Created by KewenC on 2017/3/6.
 */

public class OverOnTouchListener implements View.OnTouchListener{
    private Context context;
    private boolean mShow = true;
    private float mFirstY=1;
    private float mCurrentY=0;
    private int direction = 0;
    public OverOnTouchListener(Context context) {
        super();
        this.context=context;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        final float mTouchSlop= ViewConfiguration.get(context).getScaledTouchSlop();//获取系统认为的最低滑动距离
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN://触摸时操作
                mFirstY=event.getY();
                break;
            case MotionEvent.ACTION_MOVE://移动时操作
                mCurrentY=event.getY();
                if(mCurrentY-mFirstY>mTouchSlop){
                    direction=0;//down

                }else if(mFirstY-mCurrentY>mTouchSlop){
                    direction=1;//up
                }
                if(direction==1){
                    if(mShow){
                        sircleBtnAnim(1);
                        mShow=!mShow;
                    }
                }else if(direction==0){
                    if(!mShow){
                        sircleBtnAnim(0);
                        mShow=!mShow;
                    }
                }
                v.performClick();
                break;

            case MotionEvent.ACTION_UP://离开时操作
                break;
        }
        return false;
    }

    private void sircleBtnAnim(int flag) {

        Animator mAnimator = null;

        if(mAnimator!=null&&mAnimator.isRunning()){
            mAnimator.cancel();
        }
        if(flag==0){
            MainActivity.isShow(0);
        }else{
            MainActivity.isShow(1);

        }
    }
}
