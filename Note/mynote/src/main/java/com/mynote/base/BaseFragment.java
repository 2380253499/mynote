package com.mynote.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.DateUtils;
import com.github.baseclass.view.Loading;
import com.github.rxbus.MyConsumer;
import com.github.rxbus.MyDisposable;
import com.github.rxbus.RxBus;
import com.github.rxbus.rxjava.MyFlowableSubscriber;
import com.github.rxbus.rxjava.Rx;
import com.library.base.MyBaseFragment;
import com.library.base.ProgressLayout;
import com.mynote.Constant;

import org.reactivestreams.Subscription;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.FlowableEmitter;
import io.reactivex.annotations.NonNull;

/**
 * Created by Administrator on 2017/12/18.
 */

public abstract class BaseFragment<I extends BaseDaoImp> extends MyBaseFragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected I mDaoImp;
    protected String searchInfo;
    protected boolean isOrderByCreateTime;
    protected int dataCount;
    public int getDataCount() {
        return dataCount;
    }

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

    public void setCreateTime(LinearLayout ll_update_time, TextView tv_create_time,  TextView tv_update_time, boolean isEdit,BaseEntity entity){
        ll_update_time.setVisibility(isEdit? View.VISIBLE:View.GONE);
        if(isEdit){
            tv_create_time.setText(DateUtils.dateToString(new Date(entity.getCreateTime()),DateUtils.ymdhms));
            tv_update_time.setText(DateUtils.dateToString(new Date(entity.getUpdateTime()),DateUtils.ymdhms));
        }else{
            tv_create_time.setText(DateUtils.dateToString(new Date(),DateUtils.ymdhms));
        }
    }

    /*******************************************Rx*************************************************/
    protected Set eventSet,ioSet;

    public <T> void RXStart(final IOCallBack<T> callBack) {
        RXStart(null,false,callBack);
    }
    public <T> void RXStart(boolean hiddenLoading,final IOCallBack<T> callBack) {
        RXStart(null,hiddenLoading,callBack);
    }
    public <T> void RXStart(ProgressLayout progressLayout,final IOCallBack<T> callBack) {
        RXStart(progressLayout,false,callBack);
    }
    public <T> void RXStart(final ProgressLayout progressLayout,final boolean hiddenLoading, final IOCallBack<T> callBack) {
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
    public void addSubscription(Subscription subscription){
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
