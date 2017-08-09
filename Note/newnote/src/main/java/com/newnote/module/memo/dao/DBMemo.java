package com.newnote.module.memo.dao;

import android.database.sqlite.SQLiteDatabase;

import com.newnote.module.memo.entity.MemoBean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */
public interface DBMemo {

    List<MemoBean> selectMemo(int page);
    List<MemoBean> selectMemo(int page, String searchInfo, boolean isOrderByCreateTime);
    List<MemoBean> selectMemo(int page, String searchInfo, boolean isOrderByCreateTime, SQLiteDatabase db);
}
