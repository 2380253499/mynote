package com.fast.note.ui.main.fragment.contract;

import com.fast.note.base.BasePresenter;
import com.fast.note.base.BaseView;
import com.fast.note.ui.main.entity.SpendBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public interface AddSpendCon {
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View> {
        boolean addSpend(SpendBean bean);
    }
}
