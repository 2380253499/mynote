package com.mynote.module.joke.fragment;

import android.os.Bundle;
import android.view.View;

import com.mynote.base.BaseFragment;
import com.mynote.module.joke.bean.JokeBean;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AddJokeFragment extends BaseFragment {
    @Override
    protected int getContentView() {
        return 0;
    }

    public static AddJokeFragment newInstance() {
        return newInstance(null);
    }
    public static AddJokeFragment newInstance(JokeBean jokeBean) {
        Bundle args = new Bundle();
        AddJokeFragment fragment = new AddJokeFragment();
        fragment.setArguments(args);
        return fragment;
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
