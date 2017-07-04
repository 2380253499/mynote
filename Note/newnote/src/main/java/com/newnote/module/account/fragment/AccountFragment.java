package com.newnote.module.account.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.newnote.R;
import com.newnote.base.BaseFragment;
import com.newnote.base.BasePresenter;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/4.
 */

public class AccountFragment extends BaseFragment {
    @BindView(R.id.rv_account_list)
    RecyclerView rv_account_list;

    public static AccountFragment newInstance() {
        Bundle args = new Bundle();
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected BasePresenter initPresenter() {
        return null;
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initView() {
        rv_account_list.setNestedScrollingEnabled(false);
    }

    @Override
    protected void initData() {

    }
}
