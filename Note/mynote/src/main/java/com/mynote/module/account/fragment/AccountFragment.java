package com.mynote.module.account.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.github.baseclass.rx.IOCallBack;
import com.github.customview.MyEditText;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.module.account.adapter.AccountAdapter;
import com.mynote.module.account.bean.AccountBean;
import com.mynote.module.account.dao.imp.AccountImp;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

public class AccountFragment extends BaseFragment<AccountImp> {
    @BindView(R.id.et_search_account)
    MyEditText et_search_account;

    @BindView(R.id.rv_account)
    RecyclerView rv_account;

    AccountAdapter adapter;

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
        adapter=new AccountAdapter(mContext,R.layout.item_account,pageSize,nsv);
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
        RXStart(pl_load,new IOCallBack<List<AccountBean>>() {
            @Override
            public void call(Subscriber<? super List<AccountBean>> subscriber) {
                List<AccountBean> accountList = mDaoImp.selectAccount(page, searchInfo, isOrderByCreateTime);
                subscriber.onNext(accountList);
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(List<AccountBean> list) {
                if(isLoad){
                    pageNum++;
                    adapter.addList(list,true);
                }else{
                    pageNum=2;
                    adapter.setList(list,true);
                }
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
