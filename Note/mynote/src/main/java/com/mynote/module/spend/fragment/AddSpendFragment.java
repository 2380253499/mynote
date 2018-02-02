package com.mynote.module.spend.fragment;

import android.os.Bundle;
import android.view.View;

import com.mynote.base.BaseFragment;
import com.mynote.module.spend.bean.SpendBean;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AddSpendFragment extends BaseFragment {
    @Override
    protected int getContentView() {
        return 0;
    }

    public static AddSpendFragment newInstance( ) {
        return newInstance(null);
    }
    public static AddSpendFragment newInstance(SpendBean spendBean) {

        Bundle args = new Bundle();

        AddSpendFragment fragment = new AddSpendFragment();
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
