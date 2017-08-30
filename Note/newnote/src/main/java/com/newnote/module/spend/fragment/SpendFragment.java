package com.newnote.module.spend.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.baseclass.BasePresenter;
import com.newnote.base.BaseFragment;

/**
 * Created by Administrator on 2017/7/4.
 */

public class SpendFragment extends BaseFragment {
    public static SpendFragment newInstance() {
        Bundle args = new Bundle();

        SpendFragment fragment = new SpendFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return 0;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void onViewClick(View v) {

    }
}
