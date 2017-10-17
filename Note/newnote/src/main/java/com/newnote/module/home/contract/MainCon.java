package com.newnote.module.home.contract;

import com.github.baseclass.BasePresenter;
import com.github.baseclass.BaseView;

/**
 * Created by administartor on 2017/8/30.
 */

public interface MainCon {
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View>{

        void itemClick(int itemId);
    }
}
