package com.mynote.base;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.library.base.MyBaseFragment;
import com.mynote.Constant;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Administrator on 2017/12/18.
 */

public abstract class BaseFragment<I extends BaseDaoImp> extends MyBaseFragment {
    protected final String TAG = this.getClass().getSimpleName();
    protected I mDaoImp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        pageSize= Constant.pageSize;
        pagesize= Constant.pageSize;
        mDaoImp= getDaoImp();
        mDaoImp.setContext(mContext);
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

}
