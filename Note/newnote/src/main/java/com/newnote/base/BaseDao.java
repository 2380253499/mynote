package com.newnote.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.newnote.database.DBManager;

/**
 * Created by Administrator on 2017/7/25.
 */
public abstract class BaseDao {
    protected Context mContext;
    protected String TAG=this.getClass().getSimpleName();
    public BaseDao(Context context) {
        this.mContext=context;
    }
    public String getLimitSql(int page){
        //小于等于0查询所有数据
        if(page<=0){
            return "";
        }
        String limit=String.format(" limit "+DBManager.pageSize+" offset %d ",DBManager.pageSize*(page-1));
        return limit;
    }
    public String getLimit(int page){
        //小于等于0查询所有数据
        if(page<=0){
            return null;
        }
        return DBManager.pageSize*(page-1)+","+DBManager.pageSize;
    }
    public SQLiteDatabase getWritableDatabase(){
       return DBManager.getNewInstance(mContext).getWritableDatabase();
    }
}
