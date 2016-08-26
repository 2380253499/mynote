package com.zr.note.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zr.note.tools.ClickUtils;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseFragment <V extends BaseView,B extends BaseBiz<V>> extends IBaseFragment implements BaseView,View.OnClickListener{
    protected B fBiz;
    protected abstract B initImp();
    protected abstract int setContentView();
    protected abstract void initView();
    protected abstract void initData();
    protected abstract void viewOnClick(View v);
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(setContentView(), container, false);
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fBiz=initImp();
        initView();
        initData();
    }

    @Override
    public void onClick(View v) {
        if(!ClickUtils.isFastClick(v)){
            viewOnClick(v);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hideLoading() {

    }

    @Override
    public void showMsg(String msg) {
        showToastS(msg);
    }

    @Override
    public void onResume() {
        super.onResume();
        fBiz.attach((V) this);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        fBiz.detach();
    }
}
