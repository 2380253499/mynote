package com.fast.note.ui.main.fragment.contract;

import android.widget.ListView;

import com.fast.note.base.BasePresenter;
import com.fast.note.base.BaseView;
import com.fast.note.tools.MyDialog;
import com.fast.note.ui.main.entity.MemoBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface MemoCon {
    interface View extends BaseView{
        void selectData();
        void afterSelectData(List list);
        void hiddenSearch(boolean isDeleteAll);
    }
    interface Presenter extends BasePresenter<View>{
        void selectData(ListView lv_memo_list, boolean isOrderByCreateTime);
        MemoBean copyMemo(int position);
        void deleteMemoById(MyDialog.Builder mDialog, int id);
        boolean dataBatchCheckNotEmpty();
        void endDataBatchSelect();
        void checkAll(boolean isOrderByCreateTime);
        void cancelCheckAll(boolean isOrderByCreateTime);
        void deleteAll();

        void searchMemo(String info);
    }
}
