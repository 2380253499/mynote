package com.mynote.module.account.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.androidtools.PhoneUtils;
import com.github.rxbus.RxBus;
import com.mynote.IntentParam;
import com.mynote.R;
import com.mynote.base.BaseFragment;
import com.mynote.base.EventCallback;
import com.mynote.base.IOCallBack;
import com.mynote.event.ClearDataEvent;
import com.mynote.event.GetDataEvent;
import com.mynote.event.SaveDataEvent;
import com.mynote.module.account.bean.AccountBean;
import com.mynote.module.account.dao.imp.AccountImp;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.FlowableEmitter;

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


    @BindView(R.id.ll_update_time)
    LinearLayout ll_update_time;
    @BindView(R.id.tv_create_time)
    TextView tv_create_time;
    @BindView(R.id.tv_update_time)
    TextView tv_update_time;

    //用于更新数据
    private boolean addDataSuccess;
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
        accountBean = (AccountBean) getArguments().getSerializable(IntentParam.editAccount);
    }

    @Override
    protected void initRxBus() {
        super.initRxBus();
        getEvent(SaveDataEvent.class, new EventCallback<SaveDataEvent>() {
            @Override
            public void accept(SaveDataEvent event) {
                if(event.index== SaveDataEvent.accountIndex){
                    String userStr = et_addData_user.getText().toString().trim();
                    if (TextUtils.isEmpty(userStr)||userStr.trim().length()<=0) {
                        showToastS("账户不能为空");
                    } else {
                        String source = et_addData_source.getText().toString();
                        String user = et_addData_user.getText().toString();
                        String pwd = et_addData_pwd.getText().toString();
                        String note = et_addData_note.getText().toString();
                        AccountBean bean ;
                        if(isEdit){
                            bean=accountBean;
                            bean.setDataSource(source);
                            bean.setDataAccount(user);
                            bean.setDataPassword(pwd);
                            bean.setDataRemark(note);
                            editAccount(bean);
                        }else{
                            bean=new AccountBean();
                            bean.setDataSource(source);
                            bean.setDataAccount(user);
                            bean.setDataPassword(pwd);
                            bean.setDataRemark(note);
                            addAccount(bean);
                        }
                    }
                }
            }
        });
        getEvent(ClearDataEvent.class, new EventCallback<ClearDataEvent>() {
            @Override
            public void accept(ClearDataEvent event) {
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

    private void editAccount(AccountBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(FlowableEmitter<String> subscriber) {
                long count = mDaoImp.updateAccount(bean);
                subscriber.onNext(count>0?"修改成功":"修改失败");
                subscriber.onComplete();
            }
            @Override
            public void onMyNext(String msg) {
                addDataSuccess=true;
                showMsg(msg);
            }
        });
    }

    private void addAccount(AccountBean bean) {
        showLoading();
        RXStart(pl_load,new IOCallBack<String>() {
            @Override
            public void call(FlowableEmitter<String> subscriber) {
                long addAccount = mDaoImp.addAccount(bean);
                subscriber.onNext(addAccount>0?"添加成功":"添加失败");
                subscriber.onComplete();
            }
            @Override
            public void onMyNext(String msg) {
                showMsg(msg);
                addDataSuccess=true;
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
        if (accountBean != null) {
            isEdit = true;
            et_addData_source.setText(accountBean.getDataSource());
            et_addData_user.setText(accountBean.getDataAccount());
            et_addData_pwd.setText(accountBean.getDataPassword());
            et_addData_note.setText(accountBean.getDataRemark());
        }

        setCreateTime(ll_update_time,tv_create_time, tv_update_time,isEdit,accountBean );
    }

    @OnClick({
            R.id.tv_account_copy,
            R.id.tv_pwd_copy,
            R.id.tv_account_paste,
           R.id.tv_pwd_paste
    })
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(addDataSuccess){
            RxBus.getInstance().post(new GetDataEvent(GetDataEvent.accountIndex));
        }
    }
}
