package com.zr.note.ui.main.fragment.contract;

import android.widget.ListView;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.main.entity.AccountBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface AccountCon {
    interface View extends BaseView{
        void selectData(boolean isOrderByCreateTime);
    }
    interface Presenter extends BasePresenter<View>{
        List<AccountBean> selectData(ListView lv_account_list,boolean isOrderByCreateTime);
        AccountBean copyAccount(int position);
        void deleteAccountById(MyDialog.Builder mDialog, int id);
        void deleteAccountById(MyDialog.Builder mDialog,String[] id);
    }
}
