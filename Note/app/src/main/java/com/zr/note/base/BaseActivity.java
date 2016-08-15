package com.zr.note.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.zr.note.R;
import com.zr.note.tools.StatusBarCompat;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseActivity extends IBaseActivity{
    private int colorPrimaryDark=-1;
    private Toolbar toolbar;

    protected abstract int setContentView();
    protected abstract int initView();
    protected abstract int initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColorPrimaryDark();
        setContentView(setContentView());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolBar();
        setSupportActionBar(toolbar);
        initView();
        initData();
    }

    private void setToolBar() {

    }

    private void setColorPrimaryDark() {
        if(colorPrimaryDark==-1){
            StatusBarCompat.compat(this);
        }else{
            StatusBarCompat.compat(this,colorPrimaryDark);
        }
    }
    protected void setPrimaryDark(int colorId){
        colorPrimaryDark=colorId;
    }

}
