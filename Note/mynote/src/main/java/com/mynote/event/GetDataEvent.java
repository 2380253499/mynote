package com.mynote.event;

/**
 * Created by Administrator on 2018/2/2.
 */

public class GetDataEvent {
    public static final int accountIndex=0;
    public static final int memoIndex=1;
    public static final int jokeIndex=2;
    public static final int spendIndex=3;
    public static final int secretIndex=4;
    public int  index;
    public GetDataEvent(int index) {
        this.index=index;
    }
}
