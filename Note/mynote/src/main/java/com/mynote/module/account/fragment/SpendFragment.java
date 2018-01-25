package com.mynote.module.account.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mynote.R;
import com.mynote.base.BaseFragment;

import butterknife.BindView;

public class SpendFragment extends BaseFragment {
    @BindView(R.id.ll_tree)
    LinearLayout ll_tree;
    private boolean isCreateTime;

    public static SpendFragment newInstance() {

        Bundle args = new Bundle();

        SpendFragment fragment = new SpendFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_spend;
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
