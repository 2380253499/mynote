package com.mynote.module.account.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.adapter.LoadMoreViewHolder;
import com.github.baseclass.rx.IOCallBack;
import com.github.customview.MyEditText;
import com.mynote.R;
import com.mynote.base.BaseFragment;

import butterknife.BindView;
import rx.Subscriber;

public class AccountFragment extends BaseFragment {
    @BindView(R.id.et_search_account)
    MyEditText et_search_account;

    @BindView(R.id.rv_account)
    RecyclerView rv_account;

    LoadMoreAdapter adapter;

    public static AccountFragment newInstance() {
        Bundle args = new Bundle();
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getContentView() {
        return R.layout.fragment_account;
    }


    @Override
    protected void initView() {
        adapter=new LoadMoreAdapter<String>(mContext,R.layout._item_,pageSize,nsv) {
            @Override
            public void bindData(LoadMoreViewHolder holder, int position, String bean) {

            }
        };
        adapter.setOnLoadMoreListener(this);
        rv_account.setAdapter(adapter);
    }

    @Override
    protected void initData() {
        showProgress();
        getData(1,false);
    }

    @Override
    protected void getData(int page, boolean isLoad) {
        super.getData(page, isLoad);
        RXStart(new IOCallBack<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {

            }
            @Override
            public void onMyNext(String s) {

            }
        });
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            /*case R.id.tv_menu_copyAccount:
                mPopupwindow.dismiss();
                if(!TextUtils.isEmpty(accountBean.getDataAccount())){
                    PhoneUtils.copyText(getActivity(), accountBean.getDataAccount());
                    showToastS("复制账号成功");
                }
            break;
            case R.id.tv_menu_copyPwd:
                mPopupwindow.dismiss();
                if(TextUtils.isEmpty(accountBean.getDataPassword())){
                    showToastS("密码为空无法复制");
                }else{
                    PhoneUtils.copyText(getActivity(), accountBean.getDataPassword());
                    showToastS("复制密码成功");
                }
            break;
            case R.id.tv_menu_deleteAccount:
                mPopupwindow.dismiss();
                mPresenter.deleteAccountById(mDialog,accountBean.get_id());
            break;*/
        }
    }


}
