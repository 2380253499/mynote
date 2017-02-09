package com.zr.note.ui.secret.activity.contract.imp;

import android.content.Context;
import android.widget.ListView;

import com.github.rxjava.rxbus.MySubscriber;
import com.github.rxjava.rxbus.RxUtils;
import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.main.fragment.adapter.MemoAdapter;
import com.zr.note.ui.secret.activity.contract.SecretContract;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;

/**
 * Created by Administrator on 2017/2/6.
 */
public class SecretImp extends IPresenter<SecretContract.View> implements SecretContract.Presenter {

    private List<MemoBean> memoBeans;

    public SecretImp(Context context) {
        super(context);
    }

    @Override
    public void selectData(final ListView listView) {
        mView.showLoading();
        Subscription subscribe = Observable.create(new Observable.OnSubscribe<List<MemoBean>>() {
            @Override
            public void call(Subscriber<? super List<MemoBean>> subscriber) {
                memoBeans = DBManager.getNewInstance(mContext).selectSecret();
                subscriber.onNext(memoBeans);
                subscriber.onCompleted();
            }
        }).compose(RxUtils.appSchedulers()).subscribe(new MySubscriber<List<MemoBean>>() {
            @Override
            public void onMyNext(List<MemoBean> obj) {
                MemoAdapter memoAdapter = new MemoAdapter(mContext, obj, R.layout.item_memo);
                listView.setAdapter(memoAdapter);
            }
            @Override
            public void onResult(boolean isCompleted, Throwable e) {
                mView.hideLoading();
            }
        });
        addSubscription(subscribe);


    }

    @Override
    public void editBean(int position) {
        mView.editBean(memoBeans, position);
    }

    @Override
    public void deleteSecret(final int position) {
        mView.showLoading();
        Subscription subscribe = Observable.create(new Observable.OnSubscribe<Boolean>() {
            @Override
            public void call(Subscriber<? super Boolean> subscriber) {
                boolean result = DBManager.getNewInstance(mContext).deleteSecret(memoBeans.get(position).get_id());
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).compose(RxUtils.appSchedulers()).subscribe(new MySubscriber<Boolean>() {
            @Override
            public void onMyNext(Boolean result) {
                    mView.deleteResult(result);
            }
            @Override
            public void onResult(boolean isCompleted, Throwable e) {
                mView.hideLoading();
            }
        });
        addSubscription(subscribe);
    }
}
