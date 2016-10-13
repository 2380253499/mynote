package com.zr.note.base;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by Administrator on 2016/8/16.
 */
public abstract class IPresenter<V extends BaseView>{
    protected V mView;
    protected Handler mHandler;
    protected Context mContext;
    public IPresenter() {
        this.mHandler =new Handler(Looper.getMainLooper());
    }
    public IPresenter(Context context) {
        mContext=context;
        this.mHandler =new Handler(Looper.getMainLooper());
    }
    public void attach(V view){
        mView=view;
    }
    public void detach(){
        mView=null;
        mContext=null;
    }
}
