package com.zr.note.ui.main.fragment.contract;

import android.widget.ListView;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.main.entity.SpendBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/24.
 */
public interface SpendCon {
    interface View extends BaseView{
        void selectData( );
    }
    interface Presenter extends BasePresenter<View>{
        List<SpendBean> selectData(ListView lv_spend_list, boolean isOrderByCreateTime);
        SpendBean copySpend(int position);
        void deleteSpendById(MyDialog.Builder mDialog, int id);
        void deleteSpendById(MyDialog.Builder mDialog, String[] id);
    }
}
