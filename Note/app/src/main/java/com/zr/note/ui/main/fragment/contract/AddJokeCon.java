package com.zr.note.ui.main.fragment.contract;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.ui.main.entity.JokeBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public interface AddJokeCon {
    interface View extends BaseView {

    }
    interface Presenter extends BasePresenter<View> {
        boolean addJoke(JokeBean bean);
    }
}
