package com.mynote;

import android.app.Application;

import com.github.baseclass.view.Loading;

/**
 * Created by Administrator on 2018/2/2.
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Loading.setLoadView(R.layout.app_loading_view);
    }
}
