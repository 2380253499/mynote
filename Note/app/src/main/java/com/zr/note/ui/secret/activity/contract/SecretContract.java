package com.zr.note.ui.secret.activity.contract;

import android.widget.ListView;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;

/**
 * Created by Administrator on 2017/2/6.
 */
public interface SecretContract {
    interface View extends BaseView {

    }
    interface Presenter extends BasePresenter<View> {
        void selectData(ListView listView);
    }
}
