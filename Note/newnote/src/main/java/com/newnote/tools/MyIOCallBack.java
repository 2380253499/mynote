package com.newnote.tools;

import android.content.Context;

import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.view.Loading;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/7/7.
 */

public abstract class MyIOCallBack<T> extends IOCallBack<T> {
    public MyIOCallBack(Context context) {
        Loading.show(context);
    }
    public MyIOCallBack(Context context,boolean noLoading) {
        if(!noLoading){
            Loading.show(context);
        }
    }
    @Override
    public abstract void call(Subscriber sub);
    @Override
    public abstract void onMyNext(T item);
    @Override
    public void onMyCompleted() {
        super.onMyCompleted();
        Loading.dismissLoading();
    }

    @Override
    public void onMyError(Throwable e) {
        super.onMyError(e);
        Loading.dismissLoading();
    }
}
