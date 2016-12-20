package com.zr.note.ui.main.fragment.contract;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.ui.main.entity.SpendBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public interface AddSpendCon {
    interface View extends BaseView {

    }
    interface Presenter extends BasePresenter<View> {
        boolean addSpend(SpendBean bean);
    }
}
