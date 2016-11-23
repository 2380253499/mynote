package com.zr.note.ui.main.fragment.contract;

import android.widget.ListView;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.tools.MyDialog;
import com.zr.note.ui.main.entity.JokeBean;

import java.util.List;

/**
 * Created by Administrator on 2016/10/21.
 */
public interface JokeCon {
    interface View extends BaseView{
        void selectData();
    }
    interface Presenter extends BasePresenter<View>{
        List<JokeBean> selectData(ListView lv_memo_list, boolean isOrderByCreateTime);
        JokeBean copyJoke(int position);
        void deleteJokeById(MyDialog.Builder mDialog, int id);
        void deleteJokeById(MyDialog.Builder mDialog, String[] id);

        boolean dataBatchCheckNotEmpty();
        void endDataBatchSelect();
        void checkAll(boolean isOrderByCreateTime);
        void cancelCheckAll(boolean isOrderByCreateTime);
        void deleteAll();
    }
}
