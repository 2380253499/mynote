package com.fast.note.ui.main.fragment.contract;

import android.widget.ListView;

import com.fast.note.base.BasePresenter;
import com.fast.note.base.BaseView;
import com.fast.note.tools.MyDialog;
import com.fast.note.ui.main.entity.JokeBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public interface JokeCon {
    interface View extends BaseView{
        void selectData();
        void afterSelectData(List list);
        void hiddenSearch(boolean isDeleteAll);
    }
    interface Presenter extends BasePresenter<View>{
        void selectData(ListView lv_memo_list, boolean isOrderByCreateTime);
        JokeBean copyJoke(int position);
        void deleteJokeById(MyDialog.Builder mDialog, int id);
        void deleteJokeById(MyDialog.Builder mDialog, String[] id);

        boolean dataBatchCheckNotEmpty();
        void endDataBatchSelect();
        void checkAll(boolean isOrderByCreateTime);
        void cancelCheckAll(boolean isOrderByCreateTime);
        void deleteAll();

        void searchJoke(String info);
    }
}
