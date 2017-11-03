package com.newnote;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/11/2.
 */

public class MyApplication extends Application {
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext=getBaseContext();
    }
}
