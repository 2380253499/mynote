package com.fast.note.ui.main.fragment.contract;

import com.fast.note.base.BasePresenter;
import com.fast.note.base.BaseView;

/**
 * Created by Administrator on 2016/10/28.
 */
public interface LeftMenuCon {
    interface View extends BaseView {
        
    }
    interface Presenter extends BasePresenter<View> {
        void itemClick(int itemId);
    }
}
