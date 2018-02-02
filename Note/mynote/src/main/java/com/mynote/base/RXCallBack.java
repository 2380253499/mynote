package com.mynote.base;

import android.content.Context;

import com.github.baseclass.view.Loading;
import com.library.base.ProgressLayout;

import rx.Subscriber;

/**
 * Created by Administrator on 2018/2/2.
 */

public abstract class RXCallBack<T> {
    private Context context;
    private ProgressLayout progressLayout;

    public RXCallBack(ProgressLayout pl) {
        this.progressLayout = pl;
    }
    public RXCallBack(Context ctx, ProgressLayout pl) {
        this.context = ctx;
        this.progressLayout = pl;
    }


    public abstract void call(Subscriber<? super T> var1);

    public abstract void onMyNext(T var1);

    public void onMyCompleted() {
        if (progressLayout != null) {
            progressLayout.showContent();
            progressLayout = null;
        }
        Loading.dismissLoading();
        this.context = null;
    }

    public void onMyError(Throwable e) {
        e.printStackTrace();
        if (progressLayout != null) {
            progressLayout.showErrorText();
            progressLayout = null;
        }
        Loading.dismissLoading();
        this.context = null;
    }
}
