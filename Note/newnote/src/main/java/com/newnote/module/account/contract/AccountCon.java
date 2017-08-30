package com.newnote.module.account.contract;

import com.github.baseclass.BasePresenter;
import com.github.baseclass.BaseView;
import com.newnote.module.account.entity.AccountBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/7.
 */

public interface AccountCon {
    interface View extends BaseView{
        void getAccountList(int page, List<AccountBean> item);
    }
    interface Presenter extends BasePresenter<View>{
        void getAccountList(int page,String search,boolean orderByCreateTime);
        void getAccountList(int page,String search,boolean orderByCreateTime,boolean noLoading);
    }
}
