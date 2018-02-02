package com.mynote.module.account.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.baseclass.rx.IOCallBack;
import com.github.baseclass.rx.MySubscriber;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.event.ClearDataEvent;
import com.mynote.event.SaveDataEvent;
import com.mynote.module.account.bean.AccountBean;
import com.mynote.module.account.dao.imp.AccountImp;

import java.util.List;

import butterknife.BindView;
import rx.Subscriber;

/**
 * Created by Administrator on 2018/2/2.
 */

public class AddAccountFragment extends BaseFragment<AccountImp>{
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

    /**
     * 判断是否是编辑还是添加
     */
    private boolean isEdit;
    private AccountBean accountBean;
    @Override
    protected int getContentView() {
        return R.layout.fragment_add_account;
    }

    public static AddAccountFragment newInstance() {
       return newInstance(null);
    }
    public static AddAccountFragment newInstance(AccountBean bean) {
        Bundle args = new Bundle();
        AddAccountFragment fragment = new AddAccountFragment();
        if (bean != null) {
            args.putSerializable(IntentParam.editAccount, bean);
        }
        fragment.setArguments(args);
        return fragment;
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
        getRxBusEvent(SaveDataEvent.class, new MySubscriber<SaveDataEvent>() {
            @Override
            public void onMyNext(SaveDataEvent event) {
                if(event.index== SaveDataEvent.accountIndex){
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
//                        bean.set_id(isEdit ? accountBean.get_id() : -1);
                        if(isEdit){

                        }else{
                            addAccount(bean);
                        }
                        /*boolean b = mPresenter.addAccount(bean);
                        if (b) {
                            isPrePareSelectData = true;
                        }*/
                    }
                }
            }
        });
        getRxBusEvent(ClearDataEvent.class, new MySubscriber<ClearDataEvent>() {
            @Override
            public void onMyNext(ClearDataEvent event) {
                if(event.index== SaveDataEvent.accountIndex){
                    et_addData_source.setText(null);
                    et_addData_user.setText(null);
                    et_addData_pwd.setText(null);
                    et_addData_note.setText(null);
                    et_addData_source.requestFocus();
                }
            }
        });
    }

    private void addAccount(AccountBean bean) {
//        showLoading();
        RXStart(pl_load,new IOCallBack<List<String>>() {
            @Override
            public void call(Subscriber<? super List<String>> subscriber) {
                mDaoImp.addAccount(bean);
                subscriber.onNext(null);
                subscriber.onCompleted();
            }
            @Override
            public void onMyNext(List<String> list) {
                et_addData_source.setText(null);
                et_addData_user.setText(null);
                et_addData_pwd.setText(null);
                et_addData_note.setText(null);
                et_addData_source.requestFocus();
            }
        });
    }

    @Override
    protected void initData() {
        accountBean = (AccountBean) getArguments().getSerializable(IntentParam.editAccount);
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



}