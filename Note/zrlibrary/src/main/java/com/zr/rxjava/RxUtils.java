package com.zr.rxjava;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Administrator on 2016/12/8.
 */
public class RxUtils {
    public static Observable getObservable(){
        return Observable.create(new Observable.OnSubscribe<Object>() {
            @Override
            public void call(Subscriber<? super Object> subscriber) {

            }
        });
    }
    public abstract static class MySubscriber<T> extends Subscriber{
        private final int isComplet=0;
        private final int isError=-1;
        public abstract void onMyNext(T o);
        public void onResult(int tag) {
        }
        @Override
        public void onCompleted() {
            onResult(isComplet);
        }
        @Override
        public void onError(Throwable e) {
            onResult(isError);
        }
        @Override
        public void onNext(Object o) {
            onMyNext((T)o);
        }
    }
}
