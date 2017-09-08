/*
 * Copyright (c) 2016-2017.
 * KewenC 版权所有
 */

package com.kewenc.noti.dao;

/**
 * Created by KewenC on 2017/2/28.
 */

public class Secret {
    private final static int[] add_num={112345,154321,124680,113579,197531};
    private final static int[] reference={99,88,77,66};
    public int GetData(int flag,int x4,int x3,int x2,int x1){
        int y=reference[flag];
        return (y-x4>0?y-x4:x4-y)*1000000+(y-x3>0?y-x3:x3-y)*10000+(y-x2>0?y-x2:x2-y)*100+(y-x1)+add_num[x1%5];
    }

}
