package com.newnote.module.account.fragment.contract.imp;

import android.content.Context;

import com.github.baseclass.IPresenter;
import com.newnote.database.DBManager;
import com.newnote.module.account.entity.AccountBean;
import com.newnote.module.account.fragment.contract.AccountCon;
import com.newnote.tools.MyIOCallBack;

import java.util.List;

import rx.Subscriber;

/**
 * Created by Administrator on 2017/7/7.
 */

public class AccountImp extends IPresenter<AccountCon.View> implements AccountCon.Presenter {

    public AccountImp(Context context) {
        super(context);
    }

    @Override
    public void getAccountList(final int page,String search,boolean orderByCreateTime) {
        getAccountList(page,search,orderByCreateTime,false);
    }
    @Override
    public void getAccountList(final int page,final String search,final boolean orderByCreateTime, boolean noLoading) {
        mView.RXStart(new MyIOCallBack<List<AccountBean>>(mContext,noLoading) {
            @Override
            public void call(Subscriber sub) {
                List<AccountBean> accountBeen = DBManager.getNewInstance(mContext).selectAccount(page, search, orderByCreateTime);
                sub.onNext(accountBeen);
                sub.onCompleted();
            }
            @Override
            public void onMyNext(List<AccountBean> item) {
                mView.getAccountList(page,item);
            }
        });
    }

}