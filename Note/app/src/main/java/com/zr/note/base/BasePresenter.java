package com.zr.note.base;

import android.os.Handler;
import android.os.Looper;

/**
 * Created by Administrator on 2016/8/16.
 */
public abstract class BasePresenter<V extends BaseView> implements IPresenter<V> {
    protected V mView;
    protected Handler mHandler;

    public BasePresenter() {
        this.mHandler =new Handler(Looper.getMainLooper());
    }
    public void attach(V view){
        mView=view;
    }
    public void detach(){
        mView=null;
    }
}
