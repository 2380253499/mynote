package com.newnote.module.joke.fragment;

import android.os.Bundle;
import android.view.View;

import com.github.baseclass.BasePresenter;
import com.newnote.base.BaseFragment;

/**
 * Created by Administrator on 2017/7/4.
 */

public class JokeFragment extends BaseFragment {
    public static JokeFragment newInstance() {
        Bundle args = new Bundle();

        JokeFragment fragment = new JokeFragment();
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
