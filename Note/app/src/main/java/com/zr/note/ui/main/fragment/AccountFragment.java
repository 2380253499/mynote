package com.zr.note.ui.main.fragment;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.annotation.Tag;
import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.constant.IntentParam;
import com.zr.note.ui.constant.RxTag;
import com.zr.note.ui.main.activity.AddDataActivity;
import com.zr.note.ui.main.broadcast.AddAccountDataBro;
import com.zr.note.ui.main.broadcast.BroFilter;
import com.zr.note.ui.main.entity.AccountBean;
import com.zr.note.ui.main.fragment.contract.AccountCon;
import com.zr.note.ui.main.fragment.contract.imp.AccountImp;
import com.zr.note.ui.main.inter.AddDataInter;
import com.zr.note.ui.main.inter.DateInter;
import com.zr.note.view.MyPopupwindow;

import java.util.List;

import butterknife.BindView;

public class AccountFragment extends BaseFragment<AccountCon.View, AccountCon.Presenter> implements AccountCon.View ,DateInter.dataManageInter {

    @BindView(R.id.et_search_account)
    EditText et_search_account;
    @BindView(R.id.lv_account_list)
    ListView lv_account_list;
    private AccountBean accountBean;
    private AddAccountDataBro addDataBro;
    private boolean isCreateTime=true;
    public static AccountFragment newInstance() {
        Bundle args = new Bundle();
        AccountFragment fragment = new AccountFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addDataBro = new AddAccountDataBro(new AddDataInter.AddDataFinish() {
            @Override
            public void addDataFinish() {
                selectData();
            }
        });
        getActivity().registerReceiver(addDataBro, new IntentFilter(BroFilter.addData_account));
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
//        lv_account_list.addHeaderView(new ViewStub(getActivity()));
//        lv_account_list.addFooterView(new ViewStub(getActivity()));
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
                return true;
            }
        });
        lv_account_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                AccountBean accountBean = (AccountBean) parent.getItemAtPosition(position);
                mIntent.putExtra(IntentParam.tabIndex, 0);
                mIntent.putExtra(IntentParam.editAccount, accountBean);
                STActivity(mIntent, AddDataActivity.class);
            }
        });
        et_search_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.searchAccount(s.toString().replace(" ",""));
                mPresenter.cancelCheckAll(isCreateTime);
            }
        });
    }

    @Override
    protected void initData() {
        selectData();
    }
    @Override
    public void selectData() {
        mPresenter.selectData(lv_account_list, isCreateTime);
    }

    @Override
    public void afterSelectData(List list) {
        if(list!=null&&list.size()>0){
            et_search_account.setVisibility(View.VISIBLE);
        }else{
            et_search_account.setVisibility(View.GONE);
        }
    }

    @Override
    public void hiddenSearch(boolean isDeleteAll) {
        if(isDeleteAll){
            et_search_account.setVisibility(View.GONE);
        }
    }
    @Override
    protected void viewOnClick(View v) {
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
                mPresenter.deleteAccountById(mDialog,accountBean.get_id());
            break;
        }
    }

    @Override
    public void onDestroy() {
        getActivity().unregisterReceiver(addDataBro);
        super.onDestroy();
    }

    @Override
    public void orderByCreateTime(boolean isCreateTime) {
        if(this.isCreateTime!=isCreateTime){
            this.isCreateTime=isCreateTime;
            selectData();
        }
    }
    //开始批量选择
    @Subscribe(tags = @Tag(RxTag.dataBatchSelect_account))
    public void dataBatchSelect(Integer index){
        boolean notEmpty = mPresenter.dataBatchCheckNotEmpty();
        RxBus.get().post(RxTag.notEmpty,notEmpty);
    }
    //取消批量选择
    @Subscribe(tags = @Tag(RxTag.endDataBatchSelect_account))
    public void endDataBatchSelect(Integer index){
        mPresenter.endDataBatchSelect();
    }
    //true全选  false取消全选
    @Subscribe(tags = @Tag(RxTag.dataCheckAll_account))
    public void dataCheckAll_0(Boolean isCheckAll){
        if(isCheckAll){
            mPresenter.checkAll(isCreateTime);
        }else{
            mPresenter.cancelCheckAll(isCreateTime);
        }
    }
    //开始删除
    @Subscribe(tags = @Tag(RxTag.deleteAll_account))
    public void deleteAll_0(Integer index){
        mPresenter.deleteAll_0();
    }
}
