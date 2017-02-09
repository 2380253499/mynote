package com.zr.note.ui.secret.activity.contract;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.ui.main.entity.MemoBean;

/**
 * Created by Administrator on 2017/2/6.
 */
public interface AddSecretContract {
    interface View extends BaseView {
        void addDataResult(boolean isEdit,boolean isSuccess);
    }
    interface Presenter extends BasePresenter<View> {
        void addData(MemoBean bean);
        void editData(MemoBean bean);
    }
}
