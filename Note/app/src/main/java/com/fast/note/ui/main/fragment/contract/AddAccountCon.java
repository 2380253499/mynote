package com.fast.note.ui.main.fragment.contract;

import com.fast.note.base.BasePresenter;
import com.fast.note.base.BaseView;
import com.fast.note.ui.main.entity.AccountBean;

/**
 * Created by Administrator on 2016/10/13.
 */
public interface AddAccountCon {
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View>{
        boolean addAccount(AccountBean bean);
    }
}
