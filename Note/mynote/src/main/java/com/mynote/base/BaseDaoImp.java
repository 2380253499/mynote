package com.mynote.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.androidtools.DateUtils;
import com.google.gson.Gson;
import com.mynote.BuildConfig;
import com.mynote.database.DBManager;

import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public abstract class BaseDaoImp {
    protected String TAG=this.getClass().getSimpleName();
    protected Context mContext;
    public void setContext(Context context){
        mContext=context;
    }
    public String getLimitSql(int page){
        //小于等于0查询所有数据
        if(page<=0){
            return "";
        }
        String limit=String.format(" limit "+ DBManager.pageSize+" offset %d ",DBManager.pageSize*(page-1));
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

    public void formatList(List list){
        if(BuildConfig.DEBUG){
            Log.i(TAG+"===","==="+new Gson().toJson(list));
        }
    }
    public long string2Date(String s){
        Date date = DateUtils.stringToDate(s,"yyy-MM-dd HH:mm:ss");
        return date.getTime();
    }
}
