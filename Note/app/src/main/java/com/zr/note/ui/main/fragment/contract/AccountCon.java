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
    interface View extends BaseView {
        void selectData();
        void afterSelectData(List list);
        void hiddenSearch(boolean isDeleteAll);
    }
    interface Presenter extends BasePresenter<View> {
        void selectData(ListView lv_account_list,boolean isOrderByCreateTime);
        AccountBean copyAccount(int position);
        void deleteAccountById(MyDialog.Builder mDialog, int id);
        void deleteAccountById(MyDialog.Builder mDialog,String[] id);
        boolean dataBatchCheckNotEmpty();
        void endDataBatchSelect();
        void checkAll(boolean isOrderByCreateTime);
        void cancelCheckAll(boolean isOrderByCreateTime);
        void deleteAll_0();
        void searchAccount(String info);
    }
}
