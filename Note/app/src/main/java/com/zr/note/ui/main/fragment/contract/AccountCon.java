package com.zr.note.ui.main.fragment.contract;

import com.zr.note.base.BasePresenter;
import com.zr.note.base.BaseView;
import com.zr.note.ui.main.entity.AccountBean;

/**
 * Created by Administrator on 2016/10/13.
 */
public interface AccountCon {
    interface View extends BaseView{

    }
    interface Presenter extends BasePresenter<View>{
        boolean addAccount(AccountBean bean);
    }
}
