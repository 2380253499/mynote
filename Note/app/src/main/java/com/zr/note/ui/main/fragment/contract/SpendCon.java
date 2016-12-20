package com.zr.note.ui.main.fragment.contract;

import android.widget.ListView;

import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;
import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.main.entity.SpendBean;
import com.zr.note.ui.main.fragment.adapter.MySpendHolder;

/**
 * Created by Administrator on 2016/10/24.
 */
public interface SpendCon {
    interface View extends BaseView {
        void selectData();
        void afterSelectData(TreeNode[]yearArr,AndroidTreeView atView);

        void startToEditSpend(MySpendHolder.IconTreeItem item);
    }
    interface Presenter extends BasePresenter<View> {
        void selectData(ListView lv_spend_list, boolean isOrderByCreateTime);
        SpendBean copySpend(int position);
        void deleteSpendById(MyDialog.Builder mDialog, int id);
        void deleteSpendById(MyDialog.Builder mDialog, String[] id);

        void resetItemClickSparse();
    }
}
