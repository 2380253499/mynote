package com.zr.note.ui.main.fragment.contract.imp;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.widget.ListView;

import com.hwangjr.rxbus.RxBus;
import com.zr.note.R;
import com.zr.note.base.IPresenter;
import com.zr.note.database.DBManager;
import com.zr.note.tools.LogUtils;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.constant.RxTag;
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
    private boolean isOrderByCreateTime;
    private String searchInfo;
    public AccountImp(Context context) {
        super(context);
    }

    @Override
    public void selectData(final ListView lv_account_list,final boolean isOrderByCreateTime) {
        this.isOrderByCreateTime=isOrderByCreateTime;
        mView.showLoading();
        new Thread(new Runnable() {
            @Override
            public void run() {
                accountList = DBManager.getInstance(mContext).selectAccount(searchInfo,isOrderByCreateTime);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mView.hideLoading();
                        if(accountAdapter==null){
                            accountAdapter = new AccountAdapter(mContext, accountList, R.layout.item_account);
                            lv_account_list.setAdapter(accountAdapter);
                        }else{
                            accountAdapter.setData(accountList);
                            accountAdapter.notifyDataSetChanged();
                        }
                        mView.afterSelectData(accountList);
                    }
                });
            }
        }).start();
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
    public boolean dataBatchCheckNotEmpty() {
        if(accountAdapter.getCount()>0){
            accountAdapter.setCheck();
            accountAdapter.notifyDataSetChanged();
            return true;
        }else{
            mView.showMsg("暂无数据");
            return false;
        }
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

    @Override
    public void searchAccount(String info) {
        searchInfo=info;
        RxBus.get().post(RxTag.dataNoSelectAll,RxTag.accountDataIndex);
        accountList= DBManager.getInstance(mContext).selectAccount(searchInfo,isOrderByCreateTime);
        accountAdapter.setSearchInfo(searchInfo);
        accountAdapter.setData(accountList);
    }

    @Override
    public void deleteAll_0() {
        if(accountAdapter.getData_id()!=null&&accountAdapter.getData_id().size()>0){
            MyDialog.Builder builder=new MyDialog.Builder(mContext);
            builder.setMessage("确定删除吗?");
            builder.setNegativeButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton(new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mView.showLoading();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            int accountCount = DBManager.getInstance(mContext).selectTableCount(DBManager.T_Account_Note);
                                LogUtils.Log("====" + accountCount + "============");
                            List<Integer> data_id = accountAdapter.getData_id();
                            final boolean isDeleteAll=data_id.size()==accountCount;
                            for (int i = 0; i < data_id.size(); i++) {
//                                LogUtils.Log("===="+data_id.get(i)+"============");
                                DBManager.getInstance(mContext).deleteAccount(data_id.get(i));
                            }
                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    mView.hideLoading();
                                    mView.showMsg("删除成功");
                                    accountList = DBManager.getInstance(mContext).selectAccount(searchInfo,isOrderByCreateTime);
                                    accountAdapter.setData(accountList);
                                    if(isDeleteAll){
                                        accountAdapter.setCheck(false);
                                    }
                                    accountAdapter.notifyDataSetChanged();
                                    RxBus.get().post(RxTag.dataDeleteAllSuccess, isDeleteAll);
                                    mView.hiddenSearch(isDeleteAll);
                                }
                            });
                        }
                    }).start();
                }
            });
            builder.create().show();
        }else{
            mView.showMsg("请选择需要删除的数据");
        }

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
