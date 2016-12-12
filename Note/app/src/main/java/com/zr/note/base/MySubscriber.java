package com.zr.note.base;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/9.
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
        onMyNext((T)obj);
    }
}
