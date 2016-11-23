package com.zr.note.ui.main.fragment.contract;

import android.widget.ListView;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.main.entity.MemoBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/14.
 */
public interface MemoCon {
    interface View extends BaseView{
        void selectData();
    }
    interface Presenter extends BasePresenter<View>{
        List<MemoBean> selectData(ListView lv_memo_list, boolean isOrderByCreateTime);
        MemoBean copyMemo(int position);
        void deleteMemoById(MyDialog.Builder mDialog, int id);
        void deleteMemoById(MyDialog.Builder mDialog, String[] id);
        boolean dataBatchCheckNotEmpty();
        void endDataBatchSelect();
        void checkAll(boolean isOrderByCreateTime);
        void cancelCheckAll(boolean isOrderByCreateTime);
        void deleteAll();
    }
}
