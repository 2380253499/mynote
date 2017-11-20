package com.newnote.module.account.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.baseclass.adapter.LoadMoreAdapter;
import com.github.baseclass.view.MyPopupwindow;
import com.newnote.R;
import com.newnote.base.BaseFragment;
import com.newnote.module.account.adapter.AccountAdapter;
import com.newnote.module.account.contract.AccountCon;
import com.newnote.module.account.contract.imp.AccountImp;
import com.newnote.module.account.entity.AccountBean;

import java.util.List;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/7/4.
 */

public class AccountFragment extends BaseFragment<AccountImp> implements AccountCon.View {
    @BindView(R.id.et_search_account)
    EditText et_search_account;

    @BindView(R.id.rv_account)
    RecyclerView rv_account;

    private AccountBean accountBean;
    private AccountAdapter accountAdapter;

    private boolean orderByCreateTime;
    private String searchInfo;

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
        View menu = LayoutInflater.from(getActivity()).inflate(R.layout.popu_account_menu, null);
        TextView tv_menu_copyAccount= (TextView) menu.findViewById(R.id.tv_menu_copyAccount);
        TextView tv_menu_copyPwd= (TextView) menu.findViewById(R.id.tv_menu_copyPwd);
        TextView tv_menu_deleteAccount= (TextView) menu.findViewById(R.id.tv_menu_deleteAccount);
        tv_menu_copyAccount.setOnClickListener(this);
        tv_menu_copyPwd.setOnClickListener(this);
        tv_menu_deleteAccount.setOnClickListener(this);

        mPopupwindow = new MyPopupwindow(getActivity(), menu);

        et_search_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                searchInfo=s.toString();
                mPresenter.getAccountList(1,searchInfo,orderByCreateTime);
            }
        });
        accountAdapter=new AccountAdapter(mContext,R.layout.item_account,pageSize,null);
        accountAdapter.setOnLoadMoreListener(this);
        accountAdapter.setLongClickListener(new LoadMoreAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View view, int position) {
                accountBean = accountAdapter.getList().get(position);
                mPopupwindow.showAsDropDown(view, PhoneUtils.getPhoneWidth(getActivity()) / 2 - PhoneUtils.dip2px(getActivity(), 90), -PhoneUtils.dip2px(getActivity(), 80));
            }
        });
        accountAdapter.setClickListener(new LoadMoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int i) {
                /*AccountBean accountBean = (AccountBean) parent.getItemAtPosition(position);
                mIntent.putExtra(IntentParam.tabIndex, 0);
                mIntent.putExtra(IntentParam.editAccount, accountBean);
                STActivity(mIntent, AddDataActivity.class);*/
            }
        });
        rv_account.setNestedScrollingEnabled(false);
        rv_account.setAdapter(accountAdapter);

    }
    @Override
    protected void initData() {

        mPresenter.getAccountList(1,null,orderByCreateTime);

    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()){
            case R.id.tv_menu_copyAccount:
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
//                mPresenter.deleteAccountById(mDialog,accountBean.get_id());
                break;
        }
    }
    @Override
    public void getAccountList(int page, List<AccountBean> item) {
        accountAdapter.setSearchInfo(searchInfo);
        if(page==1){
            this.pageNum=2;
            accountAdapter.setList(item,true);
        }else{
            this.pageNum++;
            accountAdapter.addList(item,true);
        }
    }
    @Override
    public void loadMore() {
        mPresenter.getAccountList(pageNum,searchInfo,orderByCreateTime,true);
    }
}
