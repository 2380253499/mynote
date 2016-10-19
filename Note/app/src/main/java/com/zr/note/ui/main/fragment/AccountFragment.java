package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.main.entity.AccountBean;
import com.zr.note.ui.main.fragment.contract.AccountCon;
import com.zr.note.ui.main.fragment.contract.imp.AccountImp;
import com.zr.note.ui.main.inter.DeteleDataInter;
import com.zr.note.view.MyPopupwindow;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends BaseFragment<AccountCon.View, AccountCon.Presenter> implements AccountCon.View ,DeteleDataInter {

    @BindView(R.id.lv_account_list)
    ListView lv_account_list;
    private AccountBean accountBean;
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
        View menu = LayoutInflater.from(getActivity()).inflate(R.layout.popu_account_menu, null);
        TextView tv_menu_copyAccount= (TextView) menu.findViewById(R.id.tv_menu_copyAccount);
        TextView tv_menu_copyPwd= (TextView) menu.findViewById(R.id.tv_menu_copyPwd);
        TextView tv_menu_deleteAccount= (TextView) menu.findViewById(R.id.tv_menu_deleteAccount);
        tv_menu_copyAccount.setOnClickListener(this);
        tv_menu_copyPwd.setOnClickListener(this);
        tv_menu_deleteAccount.setOnClickListener(this);
        mPopupwindow = new MyPopupwindow(getActivity(), menu);
        lv_account_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                accountBean = mPresenter.copyAccount(position);
                mPopupwindow.showAsDropDown(view, PhoneUtils.getPhoneWidth(getActivity()) / 2 - PhoneUtils.dip2px(getActivity(), 90), -PhoneUtils.dip2px(getActivity(), 80));
                return false;
            }
        });
    }

    @Override
    protected void initData() {
        selectData(true);
    }
    @Override
    public void selectData(boolean isOrderByCreateTime) {
        mPresenter.selectData(lv_account_list,isOrderByCreateTime);
    }
    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_menu_copyAccount:
                mPopupwindow.dismiss();
                if(!TextUtils.isEmpty(accountBean.getDataAccount())){
                    PhoneUtils.copyText(getActivity(),accountBean.getDataAccount());
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
            break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public boolean deleteData() {
        return false;
    }
}
