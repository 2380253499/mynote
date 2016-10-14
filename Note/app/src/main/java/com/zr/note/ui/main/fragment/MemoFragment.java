package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.ui.main.fragment.contract.AddAccountCon;
import com.zr.note.ui.main.fragment.contract.imp.AddAccountImp;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.ButterKnife;

public class MemoFragment extends BaseFragment<AddAccountCon.View,AddAccountCon.Presenter> implements AddDataInter,AddAccountCon.View {

    public static MemoFragment newInstance() {
        Bundle args = new Bundle();
        MemoFragment fragment = new MemoFragment();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected AddAccountImp initPresenter() {
        return new AddAccountImp(getActivity());
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_memo;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void viewOnClick(View v) {

    }

    @Override
    public void saveData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate AddDailyReminderFragment fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
