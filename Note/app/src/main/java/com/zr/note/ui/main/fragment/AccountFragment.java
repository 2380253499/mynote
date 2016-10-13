package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.ui.main.entity.AccountBean;
import com.zr.note.ui.main.fragment.contract.AccountCon;
import com.zr.note.ui.main.fragment.contract.imp.AccountImp;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountFragment extends BaseFragment<AccountCon.View,AccountCon.Presenter> implements AddDataInter,AccountCon.View {
    @BindView(R.id.et_addData_source)
    EditText et_addData_source;
    @BindView(R.id.et_addData_user)
    EditText et_addData_user;
    @BindView(R.id.et_addData_pwd)
    EditText et_addData_pwd;
    @BindView(R.id.et_addData_note)
    EditText et_addData_note;

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

    }

    @Override
    protected void viewOnClick(View v) {

    }

    @Override
    public void saveData() {
        String userStr=et_addData_user.getText().toString().trim();
        if(TextUtils.isEmpty(userStr)){
            showToastS("账户不能为空");
        }else{
            String source = et_addData_source.getText().toString();
            String user = et_addData_user.getText().toString();
            String pwd = et_addData_pwd.getText().toString();
            String note = et_addData_note.getText().toString();
            AccountBean bean=new AccountBean();
            bean.setDataSource(source);
            bean.setDataAccount(user);
            bean.setDataPassword(pwd);
            bean.setDataRemark(note);
            mPresenter.addAccount(bean);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
