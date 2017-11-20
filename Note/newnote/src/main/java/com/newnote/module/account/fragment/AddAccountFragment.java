package com.newnote.module.account.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.baseclass.rx.MySubscriber;
import com.github.baseclass.rx.RxBus;
import com.newnote.R;
import com.newnote.base.BaseFragment;
import com.newnote.module.account.contract.imp.AccountImp;
import com.newnote.module.account.entity.AccountBean;
import com.newnote.module.home.Constant;
import com.newnote.module.home.event.AddDataEvent;

import butterknife.BindView;

public class AddAccountFragment extends BaseFragment<AccountImp> {
    @BindView(R.id.et_addData_source)
    EditText et_addData_source;
    @BindView(R.id.et_addData_user)
    EditText et_addData_user;
    @BindView(R.id.et_addData_pwd)
    EditText et_addData_pwd;
    @BindView(R.id.et_addData_note)
    EditText et_addData_note;
    @BindView(R.id.tv_account_copy)
    TextView tv_account_copy;
    @BindView(R.id.tv_pwd_copy)
    TextView tv_pwd_copy;
    @BindView(R.id.tv_pwd_paste)
    TextView tv_pwd_paste;
    @BindView(R.id.tv_account_paste)
    TextView tv_account_paste;

    private boolean isPrePareSelectData;
    /**
     * 判断是否是编辑还是添加
     */
    private boolean isEdit;
    private AccountBean accountBean;


    @Override
    protected int getContentView() {
        return R.layout.fragment_add_account;
    }

    public static AddAccountFragment newInstance(AccountBean bean) {
        Bundle args = new Bundle();
        AddAccountFragment fragment = new AddAccountFragment();
        if (bean != null) {
            args.putSerializable(Constant.IParam.editAccount, bean);
        }
        fragment.setArguments(args);
        return fragment;
    }

    public static AddAccountFragment newInstance() {
        return newInstance(null);
    }

    @Override
    protected void initView() {
        et_addData_source.requestFocus();
        tv_account_copy.setOnClickListener(this);
        tv_pwd_copy.setOnClickListener(this);
        tv_account_paste.setOnClickListener(this);
        tv_pwd_paste.setOnClickListener(this);
    }

    @Override
    protected void initRxBus() {
        super.initRxBus();
        RxBus.getInstance().getEvent(AddDataEvent.class, new MySubscriber<AddDataEvent>(){
            @Override
            public void onMyNext(AddDataEvent event) {
                if(event.index!=0){
                    return;
                }
                if (event.isAddData) {
                    saveData();
                }else{
                    clearData();
                }
            }
        });
    }

    @Override
    protected void initData() {
        accountBean = (AccountBean) getArguments().getSerializable(Constant.IParam.editAccount);
        if (accountBean != null) {
            isEdit = true;
            et_addData_source.setText(accountBean.getDataSource());
            et_addData_user.setText(accountBean.getDataAccount());
            et_addData_pwd.setText(accountBean.getDataPassword());
            et_addData_note.setText(accountBean.getDataRemark());
        }
    }

    @Override
    protected void onViewClick(View v) {
        switch (v.getId()) {
            case R.id.tv_account_copy:
                String account = et_addData_user.getText().toString();
                if (TextUtils.isEmpty(account)) {
                    showToastS("请填写数据之后复制");
                } else {
                    PhoneUtils.copyText(getActivity(), account);
                    showToastS("复制成功");
                }
                break;
            case R.id.tv_pwd_copy:
                String pwd = et_addData_pwd.getText().toString();
                if (TextUtils.isEmpty(pwd)) {
                    showToastS("请填写数据之后复制");
                } else {
                    PhoneUtils.copyText(getActivity(), pwd);
                    showToastS("复制成功");
                }
                break;
            case R.id.tv_pwd_paste:
                et_addData_pwd.setText(PhoneUtils.pasteText(getActivity()));
                break;
            case R.id.tv_account_paste:
                et_addData_user.setText(PhoneUtils.pasteText(getActivity()));
                break;
        }
    }

    public void saveData() {
        String userStr = et_addData_user.getText().toString().trim();
        if (TextUtils.isEmpty(userStr)) {
            showToastS("账户不能为空");
        } else {
            String source = et_addData_source.getText().toString();
            String user = et_addData_user.getText().toString();
            String pwd = et_addData_pwd.getText().toString();
            String note = et_addData_note.getText().toString();
            AccountBean bean = new AccountBean();
            bean.setDataSource(source);
            bean.setDataAccount(user);
            bean.setDataPassword(pwd);
            bean.setDataRemark(note);
            bean.set_id(isEdit ? accountBean.get_id() : -1);
            mPresenter.addOrEditAccount(bean);
        }

    }


    public void clearData() {
        et_addData_source.setText(null);
        et_addData_user.setText(null);
        et_addData_pwd.setText(null);
        et_addData_note.setText(null);
        et_addData_source.requestFocus();
    }

    public void onDestroyView() {
        super.onDestroyView();
    }


}
