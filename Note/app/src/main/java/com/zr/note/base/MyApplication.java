package com.zr.note.base;

import android.app.Application;

/**
 * Created by Administrator on 2016/12/8.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        /*if (LeakCanary.isInAnalyzerProcess(this)) {
            return;
        }
        LeakCanary.install(this);*/
    }
}
