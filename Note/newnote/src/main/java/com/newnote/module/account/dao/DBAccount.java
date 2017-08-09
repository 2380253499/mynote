package com.newnote.module.account.dao;

import android.database.sqlite.SQLiteDatabase;

import com.newnote.module.account.entity.AccountBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */
public interface DBAccount {

    List<AccountBean> selectAccount(int page);
    List<AccountBean> selectAccount(int page,String searchInfo, boolean isOrderByCreateTime);
    List<AccountBean> selectAccount(int page,String searchInfo, boolean isOrderByCreateTime, SQLiteDatabase db);
}
