package com.mynote.module.secret.activity.contract.imp;

/**
 * Created by Administrator on 2017/2/6.
 */
/*public class AddSecretImp extends IPresenter<AddSecretContract.View> implements AddSecretContract.Presenter {
    public AddSecretImp(Context context) {
        super(context);
    }
    @Override
    public void addData(final SecretBean bean) {
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
                    mView.addDataResult(false,true);
                }else{
                    mView.addDataResult(false,false);
                }
            }
            @Override
            public void onResult(boolean isCompleted, Throwable e) {
                mView.hideLoading();
            }
        });
        addSubscription(subscribe);
    }

    @Override
    public void editData(final SecretBean bean) {
        mView.showLoading();
        Subscription subscribe = Observable.create(new Observable.OnSubscribe<Long>() {
            @Override
            public void call(Subscriber<? super Long> subscriber) {
                long result = DBManager.getNewInstance(mContext).updateSecret(bean);
                subscriber.onNext(result);
                subscriber.onCompleted();
            }
        }).compose(RxUtils.appSchedulers()).subscribe(new MySubscriber<Long>() {
            @Override
            public void onMyNext(Long result) {
                if(result>0){
                    mView.addDataResult(true,true);
                }else{
                    mView.addDataResult(true,false);
                }
            }
            @Override
            public void onResult(boolean isCompleted, Throwable e) {
                mView.hideLoading();
            }
        });
        addSubscription(subscribe);
    }
}*/
