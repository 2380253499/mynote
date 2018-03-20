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
import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.rx.RxUtils;
import com.github.baseclass.view.Loading;
import com.library.base.MyBaseActivity;
import com.library.base.ProgressLayout;
import com.mynote.BuildConfig;
import com.mynote.Constant;
import com.mynote.R;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

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
    public void countDown(TextView textView) {
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
                    public void onCompleted() {
                        textView.setEnabled(true);
                        textView.setText("获取验证码");
                    }

                    @Override
                    public void onNext(Long aLong) {
                        textView.setText("剩下" + aLong + "s");
                    }

                    @Override
                    public void onError(Throwable e) {
                    }
                });
        addSubscription(subscribe);
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

    public <T> void RXStart(final IOCallBack<T> callBack) {
        RXStart(null,callBack,false);
    }
    public <T> void RXStart(boolean hiddenLoading,final IOCallBack<T> callBack) {
        RXStart(null,callBack,hiddenLoading);
    }
    public <T> void RXStart(ProgressLayout progressLayout,final IOCallBack<T> callBack) {
        RXStart(progressLayout,callBack,false);
    }
    public <T> void RXStart(ProgressLayout progressLayout, final IOCallBack<T> callBack,boolean hiddenLoading) {
        Subscription subscribe = Observable.create(new Observable.OnSubscribe<T>() {
            public void call(Subscriber<? super T> subscriber) {
                callBack.call(subscriber);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Subscriber<T>() {
            public void onCompleted() {
                if(progressLayout!=null){
                    progressLayout.showContent();
                }
                if(!hiddenLoading){
                    Loading.dismissLoading();
                }
                callBack.onMyCompleted();
            }

            public void onError(Throwable e) {
                if(progressLayout!=null){
                    progressLayout.showErrorText();
                }
                if(!hiddenLoading){
                    Loading.dismissLoading();
                }
                callBack.onMyError(e);
            }

            public void onNext(T t) {
                callBack.onMyNext(t);
            }
        });
        this.addSubscription(subscribe);
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

}

