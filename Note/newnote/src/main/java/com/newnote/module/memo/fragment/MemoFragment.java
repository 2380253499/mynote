package com.newnote.module.memo.fragment;

import android.os.Bundle;

import com.newnote.base.BaseFragment;
import com.newnote.base.BasePresenter;

/**
 * Created by Administrator on 2017/7/4.
 */

public class MemoFragment extends BaseFragment {
    public static MemoFragment newInstance() {
        Bundle args = new Bundle();
        MemoFragment fragment = new MemoFragment();
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
}
