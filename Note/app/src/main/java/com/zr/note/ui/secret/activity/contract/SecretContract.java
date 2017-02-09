package com.zr.note.ui.secret.activity.contract;

import android.widget.ListView;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.ui.main.entity.MemoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/2/6.
 */
public interface SecretContract {
    interface View extends BaseView {

        void editBean(List<MemoBean> memoBeans,int position);
    }
    interface Presenter extends BasePresenter<View> {
        void selectData(ListView listView);

        void editBean(int position);
    }
}
