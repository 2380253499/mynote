package com.zr.note.ui.main.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.zr.note.R;
import com.zr.note.base.BaseFragment;
import com.zr.note.tools.MyDialog;
import com.zr.note.tools.PhoneUtils;
import com.zr.note.ui.main.fragment.contract.AccountCon;
import com.zr.note.ui.main.fragment.contract.imp.AccountImp;
import com.zr.note.view.MyPopupwindow;

import butterknife.BindView;

public class AccountFragment extends BaseFragment<AccountCon.View, AccountCon.Presenter> implements AccountCon.View {

    @BindView(R.id.lv_account_list)
    ListView lv_account_list;

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

    int popuViewWidth;
    @Override
    protected void initView() {
        mPopupwindow=new MyPopupwindow(getActivity(),R.layout.popu_data_menu);
        lv_account_list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                mPopupwindow.showAsDropDown(view, PhoneUtils.getPhoneWidth(getActivity()) / 2 - PhoneUtils.dip2px(getActivity(),90), -PhoneUtils.dip2px(getActivity(), 80));
                return false;
            }
        });
        lv_account_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyDialog.Builder myDialog=new MyDialog.Builder(getActivity());
                View inflate = LayoutInflater.from(getActivity()).inflate(R.layout.popu_data_menu, null);
                myDialog.setContentView(inflate);
                myDialog.setTitle("确认删除");
                myDialog.setMessage("确认删除这条说说吗?");
                myDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                myDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                myDialog.create().show();
            }
        });
    }

    @Override
    protected void initData() {
        selectData();
    }
    public void selectData(){
        mPresenter.selectData(lv_account_list);
    }
    @Override
    protected void viewOnClick(View v) {

    }


}
