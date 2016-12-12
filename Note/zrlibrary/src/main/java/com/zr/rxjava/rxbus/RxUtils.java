package com.zr.rxjava.rxbus;

import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/8.
 */
public class RxUtils {
    public abstract static class MySubscriber<T> extends Subscriber{
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
}
