package com.zr.note.base;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Administrator on 2016/8/16.
 */
public abstract class BaseBiz<V> {
    protected V mView;
    protected Handler mHandler;

    public BaseBiz() {
        this.mHandler =new Handler(Looper.getMainLooper());
    }

    protected void attach(V view){
        mView=view;
    }
    protected void detach(){
        mView=null;
    }
}
