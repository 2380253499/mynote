package com.fast.note.ui.main.fragment.contract;

import com.fast.note.base.BasePresenter;
import com.fast.note.base.BaseView;
import com.fast.note.ui.main.entity.JokeBean;

/**
 * Created by Administrator on 2016/10/24.
 */
public interface AddJokeCon {
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View>{
        boolean addJoke(JokeBean bean);
    }
}
