package com.mynote.module.account.fragment;

import android.os.Bundle;
import android.view.View;

import com.mynote.R;
import com.mynote.base.BaseFragment;

public class JokeFragment extends BaseFragment {
    public static JokeFragment newInstance() {
        Bundle args = new Bundle();
        JokeFragment fragment = new JokeFragment();
        fragment.setArguments(args);
        return fragment;
    }
    protected int getContentView() {
        return R.layout.fragment_joke;
    }
    @Override
    protected void initView() {
    }
    @Override
    protected void initData() {
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            /*case R.id.tv_menu_copyJokeContent:
                mPopupwindow.dismiss();
                String jokeContent = jokeBean.getDataContent();
                if (!TextUtils.isEmpty(jokeContent)) {
                    PhoneUtils.copyText(getActivity(),jokeContent);
                    showToastS("复制成功");
                }
                break;
            case R.id.tv_menu_deleteJoke:
                mPopupwindow.dismiss();
                mPresenter.deleteJokeById(mDialog,jokeBean.get_id());
            break;*/
        }
    }



}
