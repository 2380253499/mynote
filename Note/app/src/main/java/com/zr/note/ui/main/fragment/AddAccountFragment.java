package com.zr.note.ui.main.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.AccountBean;
import com.zr.note.ui.main.fragment.contract.AddAccountCon;
import com.zr.note.ui.main.fragment.contract.imp.AddAccountImp;
import com.zr.note.ui.main.inter.AddDataInter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddAccountFragment extends BaseFragment<AddAccountCon.View,AddAccountCon.Presenter> implements AddDataInter,AddAccountCon.View {
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

    /**
     * 判断是否是编辑还是添加
     */
    private boolean isEdit;
    private AccountBean accountBean;

    @Override
    protected AddAccountImp initPresenter() {
        return new AddAccountImp(getActivity());
    }

    @Override
    protected int setContentView() {
        return R.layout.fragment_add_account;
    }

    public static AddAccountFragment newInstance(AccountBean bean) {
        Bundle args = new Bundle();
        AddAccountFragment fragment = new AddAccountFragment();
        if(bean!=null){
            args.putSerializable(IntentParam.editAccount,bean);
        }
        fragment.setArguments(args);
        return fragment;
    }
    public static AddAccountFragment newInstance() {
        return newInstance(null);
    }
    @Override
    protected void initView() {
        tv_account_copy.setOnClickListener(this);
        tv_pwd_copy.setOnClickListener(this);
    }

    @Override
    protected void initData() {
        accountBean = (AccountBean) getArguments().getSerializable(IntentParam.editAccount);
        if(accountBean !=null){
            isEdit=true;
            et_addData_source.setText(accountBean.getDataSource());
            et_addData_user.setText(accountBean.getDataAccount());
            et_addData_pwd.setText(accountBean.getDataPassword());
            et_addData_note.setText(accountBean.getDataRemark());
        }
    }

    @Override
    protected void viewOnClick(View v) {
        switch (v.getId()){
            case R.id.tv_account_copy:
                String account = et_addData_user.getText().toString();
                if(TextUtils.isEmpty(account)){
                    showToastS("请填写数据之后复制");
                }else{
                    PhoneUtils.copyText(getActivity(),account);
                    showToastS("复制成功");
                }
            break;
            case R.id.tv_pwd_copy:
                String pwd = et_addData_pwd.getText().toString();
                if(TextUtils.isEmpty(pwd)){
                    showToastS("请填写数据之后复制");
                } else {
                    PhoneUtils.copyText(getActivity(),pwd);
                    showToastS("复制成功");
                }
            break;
        }
    }

    @Override
    public boolean saveData() {
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
            bean.set_id(isEdit ? accountBean.get_id() : -1);
            boolean b = mPresenter.addAccount(bean);
            if(b){
                mIntent.setAction(BroFilter.addData_account);
                mIntent.putExtra(BroFilter.isAddData, true);
                mIntent.putExtra(BroFilter.isAddData_index,BroFilter.index_0);
                getActivity().sendBroadcast(mIntent);
            }
            return b;
        }
        return false;
    }

    @Override
    public void clearData() {
        et_addData_source.setText(null);
        et_addData_user.setText(null);
        et_addData_pwd.setText(null);
        et_addData_note.setText(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate AddMemoFragment fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }
}
