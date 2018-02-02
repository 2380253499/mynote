package com.mynote.module.memo.fragment;

import android.os.Bundle;
import android.view.View;

import com.mynote.base.BaseFragment;
import com.mynote.module.home.inter.AddDataInter;
import com.mynote.module.memo.bean.MemoBean;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AddMemoFragment extends BaseFragment implements AddDataInter {
    @Override
    protected int getContentView() {
        return 0;
    }

    public static AddMemoFragment newInstance() {
        return newInstance(null);
    }
    public static AddMemoFragment newInstance(MemoBean memoBean) {
        Bundle args = new Bundle();

        AddMemoFragment fragment = new AddMemoFragment();
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

    @Override
    public boolean saveData() {
        return false;
    }

    @Override
    public void clearData() {

    }
}
