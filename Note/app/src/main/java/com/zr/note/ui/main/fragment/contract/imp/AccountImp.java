package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.main.entity.AccountBean;
import com.zr.note.ui.main.fragment.adapter.AccountAdapter;
import com.zr.note.ui.main.fragment.contract.AccountCon;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public class AccountImp extends IPresenter<AccountCon.View> implements AccountCon.Presenter{

    private List<AccountBean> accountList;
    private AccountAdapter  accountAdapter;
    public AccountImp(Context context) {
        super(context);
    }

    @Override
    public List<AccountBean> selectData(ListView lv_account_list,boolean isOrderByCreateTime) {
        accountList = DBManager.getInstance(mContext).selectAccount(isOrderByCreateTime);
        accountAdapter = new AccountAdapter(mContext, accountList, R.layout.item_account);
        lv_account_list.setAdapter(accountAdapter);
        return accountList;
    }
    public void batchDelete(){

    }
    @Override
    public AccountBean copyAccount(int position) {
        return accountList.get(position);
    }

    @Override
    public void deleteAccountById(MyDialog.Builder mDialog,final int id) {
        mDialog=new MyDialog.Builder(mContext);
        mDialog.setMessage(mContext.getString(R.string.delete_data));
        mDialog.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setPositiveButton(getDeleteAccountListener(id));
        mDialog.create().show();
    }

    @Override
    public void deleteAccountById(MyDialog.Builder mDialog,final String[] id) {
        mDialog=new MyDialog.Builder(mContext);
        mDialog.setMessage(mContext.getString(R.string.delete_data));
        mDialog.setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        mDialog.setPositiveButton(getDeleteAccountListener(id));
        mDialog.create().show();
    }

    @Override
    public void dataBatchCheck() {
        accountAdapter.setCheck();
        accountAdapter.notifyDataSetChanged();
    }

    @Override
    public void endDataBatchSelect() {
        accountAdapter.setCheck(false);
        accountAdapter.notifyDataSetChanged();
    }

    @Override
    public void checkAll(boolean isOrderByCreateTime) {
        accountAdapter.checkAll(isOrderByCreateTime);
        accountAdapter.notifyDataSetChanged();
    }

    @Override
    public void cancelCheckAll(boolean isOrderByCreateTime) {
        accountAdapter.cancelCheckAll(isOrderByCreateTime);
        accountAdapter.notifyDataSetChanged();
    }

    @NonNull
    private DialogInterface.OnClickListener getDeleteAccountListener(final String[] id) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                boolean flag = DBManager.getInstance(mContext).deleteAccount(id);
                if(flag){
                    mView.showMsg("删除成功");
                    mView.selectData();
                }else {
                    mView.showMsg("删除失败");
                }
            }
        };
    }
    @NonNull
    private DialogInterface.OnClickListener getDeleteAccountListener(final int id) {
        return new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                boolean flag = DBManager.getInstance(mContext).deleteAccount(id);
                if(flag){
                    mView.showMsg("删除成功");
                    mView.selectData();
                }else {
                    mView.showMsg("删除失败");
                }
            }
        };
    }
}
