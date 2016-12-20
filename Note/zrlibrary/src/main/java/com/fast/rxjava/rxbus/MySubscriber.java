package com.fast.rxjava.rxbus;

import android.util.Log;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/12.
 */
public abstract class MySubscriber<T> extends Subscriber {
    public abstract void onMyNext(T obj);
    public void onResult(boolean isCompleted) {
    }
    @Override
    public void onCompleted() {
        onResult(true);
    }
    @Override
    public void onError(Throwable e) {
        onResult(false);
    }
    @Override
    public void onNext(Object obj) {
        try {
            onMyNext((T)obj);
        }catch (Exception e){
            Log.e("******error******", e + "***error***" + e.getMessage());
            onMyNext(null);
        }
    }
}
