package com.zr.note.base;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zr.note.R;
import com.zr.note.tools.StatusBarCompat;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseActivity<V,B extends BaseBiz<V>> extends IBaseActivity implements View.OnClickListener{
    private int colorPrimaryDark=-1;
    private Toolbar toolbar;
    protected B mBiz;
    protected abstract B initImp();
    protected abstract int setContentView();
    protected abstract void initView();
    protected abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setColorPrimaryDark();
        setContentView(setContentView());
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setToolBarStyle();
        setSupportActionBar(toolbar);
        mBiz=initImp();
        initView();
        initData();
    }

    private void setToolBarStyle() {

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

    @Override
    protected void onResume() {
        super.onResume();
        mBiz.attach((V) this);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mBiz.detach();
    }

}
