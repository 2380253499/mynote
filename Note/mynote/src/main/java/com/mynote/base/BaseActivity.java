package com.mynote.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.DateUtils;
import com.github.androidtools.StatusBarUtil;
import com.github.androidtools.inter.MyOnClickListener;
import com.github.baseclass.adapter.MyLoadMoreAdapter;
import com.github.baseclass.view.Loading;
import com.github.rxbus.MyConsumer;
import com.github.rxbus.MyDisposable;
import com.github.rxbus.RxBus;
import com.github.rxbus.rxjava.MyFlowableSubscriber;
import com.github.rxbus.rxjava.Rx;
import com.library.base.MyBaseActivity;
import com.library.base.ProgressLayout;
import com.mynote.BuildConfig;
import com.mynote.Constant;
import com.mynote.R;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableSubscriber;
import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2017/12/18.
 */

public abstract class BaseActivity<I extends BaseDaoImp> extends MyBaseActivity implements MyLoadMoreAdapter.OnLoadMoreListener {
    protected final String TAG = this.getClass().getSimpleName();
    protected long mExitTime=0;
    protected Handler mHandler;
    protected I mDaoImp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        pageSize= Constant.pageSize;
        pagesize= Constant.pageSize;
        mDaoImp= getDaoImp();
        if(mDaoImp!=null){
            mDaoImp.setContext(this);
        }
        setTitleBackgroud(R.color.colorPrimaryDark);
        super.onCreate(savedInstanceState);
        StatusBarUtil.setColor(this, ContextCompat.getColor(mContext, com.library.R.color.colorPrimaryDark),0);
        mHandler=new Handler(getMainLooper());
    }

    private I getDaoImp(){
        Type genericSuperclass = getClass().getGenericSuperclass();
        if(genericSuperclass instanceof ParameterizedType){
            //参数化类型
            ParameterizedType parameterizedType= (ParameterizedType) genericSuperclass;
            //返回表示此类型实际类型参数的 Type 对象的数组
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            try {
                mDaoImp=((Class<I>)actualTypeArguments[0]).newInstance();
            } catch (java.lang.InstantiationException e) {
                mDaoImp=null;
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                mDaoImp=null;
                e.printStackTrace();
            }
        }else{
            mDaoImp=null;
        }
        return mDaoImp;
    }
    public void Log(String log){
        Log.i("===",log);
    }
    @Override
    protected void setClickListener() {
        super.setClickListener();
        if (BuildConfig.DEBUG && app_title != null) {
            app_title.setOnClickListener(new MyOnClickListener() {
                @Override
                protected void onNoDoubleClick(View v) {
                }
            });
        }
    }
    /*public void countDown(TextView textView) {
        textView.setEnabled(false);
        final long count = 30;
        Subscription subscribe = Observable.interval(1, TimeUnit.SECONDS)
                .take(31)//计时次数
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long integer) {
                        return count - integer;
                    }
                })
                .compose(RxUtils.appSchedulers())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onMyCompleted() {
                        textView.setEnabled(true);
                        textView.setText("获取验证码");
                    }

                    @Override
                    public void onMyNext(Long aLong) {
                        textView.setText("剩下" + aLong + "s");
                    }

                    @Override
                    public void onMyError(Throwable e) {
                    }
                });
        addSubscription(subscribe);
    }*/

    private org.reactivestreams.Subscription subscription;
    public void countDown(final TextView textView) {
        textView.setEnabled(false);
        final long count = 30;
        Flowable.interval(1, TimeUnit.SECONDS)
                .take(31)
                .map(integer -> count - integer)
                .subscribeOn(io.reactivex.schedulers.Schedulers.io())
                .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
                .subscribe(new FlowableSubscriber<Long>() {
                    @Override
                    public void onSubscribe(@NonNull org.reactivestreams.Subscription s) {
                        subscription = s;
                        s.request(Long.MAX_VALUE);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        textView.setText("剩下" + aLong + "s");
                    }

                    @Override
                    public void onError(Throwable t) {
                    }

                    @Override
                    public void onComplete() {
                        textView.setEnabled(true);
                        textView.setText("获取验证码");
                    }
                });
        addSubscription(subscription);
    }




    public int getAppVersionCode() {
        Context context = mContext;
        int versioncode = 1;
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = pi.versionName;
            versioncode = pi.versionCode;
            return versioncode;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versioncode;
    }

    public String getAppVersionName() {
        Context context = mContext;
        int versioncode = 1;
        String versionName = "V1.0.0";
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            return versionName;
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }



    public void setCreateTime(LinearLayout ll_update_time, TextView tv_create_time, TextView tv_update_time, boolean isEdit, BaseEntity entity){
        ll_update_time.setVisibility(isEdit? View.VISIBLE:View.GONE);
        if(isEdit){
            tv_update_time.setText(DateUtils.dateToString(new Date(entity.getCreateTime()),DateUtils.ymdhms));
            tv_create_time.setText(DateUtils.dateToString(new Date(entity.getUpdateTime()),DateUtils.ymdhms));
        }else{
            tv_create_time.setText(DateUtils.dateToString(new Date(),DateUtils.ymdhms));
        }
    }
    /*******************************************Rx*************************************************/
    protected Set eventSet,ioSet;

    public <T> void RXStart(final com.mynote.base.IOCallBack<T> callBack) {
        RXStart(null,false,callBack);
    }
    public <T> void RXStart(boolean hiddenLoading,final com.mynote.base.IOCallBack<T> callBack) {
        RXStart(null,hiddenLoading,callBack);
    }
    public <T> void RXStart(ProgressLayout progressLayout,final com.mynote.base.IOCallBack<T> callBack) {
        RXStart(progressLayout,false,callBack);
    }
    public <T> void RXStart(final ProgressLayout progressLayout,final boolean hiddenLoading, final com.mynote.base.IOCallBack<T> callBack) {
        org.reactivestreams.Subscription start = Rx.start(new MyFlowableSubscriber<T>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<T> emitter) {
                callBack.call(emitter);
            }
            @Override
            public void onNext(T obj) {
                callBack.onMyNext(obj);
            }
            @Override
            public void onComplete() {
                super.onComplete();
                if(progressLayout!=null){
                    progressLayout.showContent();
                }
                if(!hiddenLoading){
                    Loading.dismissLoading();
                }
                callBack.onMyCompleted();
            }
            @Override
            public void onError(Throwable t) {
                super.onError(t);
                if(progressLayout!=null){
                    progressLayout.showErrorText();
                }
                if(!hiddenLoading){
                    Loading.dismissLoading();
                }
                callBack.onMyError(t);
            }
        });
        addSubscription(start);
    }
    public void addSubscription(MyDisposable disposable){
        if(eventSet==null){
            eventSet=new HashSet();
        }
        eventSet.add(disposable);
    }
    public void addSubscription(org.reactivestreams.Subscription subscription){
        if(ioSet==null){
            ioSet=new HashSet();
        }
        ioSet.add(subscription);
    }

    public <T>void getEvent(Class<T> clazz,final EventCallback<T> callback){
        MyDisposable event = RxBus.getInstance().getEvent(clazz, new MyConsumer<T>() {
            @Override
            public void onAccept(T event) {
                callback.accept(event);
            }
        });
        addSubscription(event);
    }
    public <T>void getEventReplay(Class<T> clazz,final EventCallback<T> callback){
        MyDisposable event = RxBus.getInstance().getEventReplay(clazz, new MyConsumer<T>() {
            @Override
            public void onAccept(T event) {
                callback.accept(event);
            }
        });
        addSubscription(event);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(eventSet!=null){
            RxBus.getInstance().dispose(eventSet);
        }
        if (ioSet != null) {
            Rx.cancelSubscription(ioSet);
        }
    }

}

