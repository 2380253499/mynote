package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.ui.main.fragment.contract.AccountCon;
import com.zr.note.ui.main.fragment.contract.imp.AccountImp;

import butterknife.BindView;

public class AccountFragment extends BaseFragment<AccountCon.View, AccountCon.Presenter> implements AccountCon.View {

    @BindView(R.id.lv_account_list)
    ListView lv_account_list;

    public static AccountFragment newInstance() {
        Bundle args = new Bundle();
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected AccountImp initPresenter() {
        return new AccountImp(getActivity());
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_account;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        mPresenter.selectData(lv_account_list);

    }

    @Override
    protected void viewOnClick(View v) {

    }


}
