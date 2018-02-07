package com.mynote.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.view.Loading;
import com.library.base.MyBaseFragment;
import com.library.base.ProgressLayout;
import com.mynote.Constant;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2017/12/18.
 */

public abstract class BaseFragment<I extends BaseDaoImp> extends MyBaseFragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected I mDaoImp;
    protected String searchInfo;
    protected boolean isOrderByCreateTime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        pageSize= Constant.pageSize;
        pagesize= Constant.pageSize;
        mDaoImp= getDaoImp();
        if (mDaoImp != null) {
            mDaoImp.setContext(getActivity());
        }
        super.onCreate(savedInstanceState);
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
    private void imgReset(WebView webview) {
        webview.loadUrl("javascript:(function(){" +
                "var objs = document.getElementsByTagName('img'); " +
                "for(var i=0;i<objs.length;i++)  " +
                "{"
                + "var img = objs[i];   " +
                " img.style.maxWidth = '100%';img.style.height='auto';" +
                "}" +
                "})()");
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

}
