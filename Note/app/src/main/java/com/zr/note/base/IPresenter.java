package com.zr.note.base;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;

/**
 * Created by Administrator on 2016/8/16.
 */
public abstract class IPresenter<V extends BaseView>{
    protected V mView;
    protected Handler mHandler;
    protected Context mContext;
    protected Intent mIntent;
    public IPresenter(Context context) {
        mContext=context;
        this.mHandler =new Handler(Looper.getMainLooper());
    }
    public void attach(V view){
        mView=view;
    }
    public void detach(){
        mView=null;
        this.mHandler=null;
        mIntent=null;
        mContext=null;
    }
    protected Intent getmIntent(){
        return mIntent==null?new Intent():mIntent;
    }
    protected Resources getR(){
        return mContext.getResources();
    }
    protected String getStr(int resId){
        return mContext==null?"":mContext.getResources().getString(resId);
    }
}
