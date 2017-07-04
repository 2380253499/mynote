package com.newnote.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.fragment.IBaseFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/8/4.
 */
public abstract class BaseFragment <P extends BasePresenter> extends IBaseFragment implements BaseView{
    protected P mPresenter;
    protected abstract P initPresenter();
    protected abstract int getContentView();
    protected abstract void initView();
    protected abstract void initData();
    protected int pageNum=1;
    protected int pageSize=25;
    protected Unbinder mUnBind;
    /************************************************************/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(getContentView(), container, false);
        mUnBind = ButterKnife.bind(this, view);
        return view;
    }
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter= initPresenter();
        if(mPresenter!=null){
            mPresenter.attach(this);
        }
        initView();
        initData();
    }
    protected void onViewClick(View v) {
        /*if(!ClickUtils.isFastClick(v)){
            viewOnClick(v);
        }*/
    }
    @Override
    public void showMsg(String msg) {

    }
    @Override
    public void showLoading() {
        super.showLoading();
    }
    @Override
    public void hideLoading() {
        dismissLoading();
    }
    @Override
    public void actFinish() {
        getActivity().finish();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mPresenter!=null){
            mPresenter.detach();
        }
        mUnBind.unbind();
    }
}
