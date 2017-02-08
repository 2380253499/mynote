package com.zr.note.ui.secret.activity.contract.imp;

import android.content.Context;

import com.github.rxjava.rxbus.MySubscriber;
import com.github.rxjava.rxbus.RxUtils;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.secret.activity.contract.AddSecretContract;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/2/6.
 */
public class AddSecretImp extends IPresenter<AddSecretContract.View> implements AddSecretContract.Presenter {
    public AddSecretImp(Context context) {
        super(context);
    }
    @Override
    public void addData(final MemoBean bean) {
        mView.showLoading();
        Subscription subscribe = Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                long result = DBManager.getNewInstance(mContext).addSecret(bean);
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).compose(RxUtils.appSchedulers()).subscribe(new MySubscriber<Long>() {
            @Override
            public void onMyNext(Long result) {
                if(result>0){
                    mView.addDataResult(true);
                }else{
                    mView.addDataResult(false);
                }
            }
            @Override
            public void onResult(boolean isCompleted, Throwable e) {
                mView.hideLoading();
            }
        });
        addSubscription(subscribe);
    }
}
