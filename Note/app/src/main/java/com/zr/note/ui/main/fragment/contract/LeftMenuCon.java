package com.zr.note.ui.main.fragment.contract;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;

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
