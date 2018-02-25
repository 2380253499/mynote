package com.mynote.module.secret.activity.contract.imp;

/**
 * Created by Administrator on 2017/2/6.
 */
/*
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
                JokeAdapter memoAdapter = new JokeAdapter(mContext, obj, R.layout.item_memo);
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
*/
