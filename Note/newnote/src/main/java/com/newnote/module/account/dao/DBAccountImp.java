package com.newnote.module.account.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.github.androidtools.DateUtils;
import com.newnote.base.BaseDao;
import com.newnote.database.DBConstant;
import com.newnote.module.account.entity.AccountBean;

import java.util.ArrayList;
import java.util.List;

import static com.newnote.database.DBManager.T_Account_Note;

/**
 * Created by Administrator on 2017/7/25.
 */
public class DBAccountImp extends BaseDao implements DBAccount {

    public DBAccountImp(Context context) {
        super(context);
    }
    @Override
    public List<AccountBean> selectAccount(int page) {
        return selectAccount(page,null, true);
    }

    @Override
    public List<AccountBean> selectAccount(int page, String searchInfo, boolean isOrderByCreateTime) {
        return selectAccount(page,searchInfo, isOrderByCreateTime,getWritableDatabase());
    }
    @Override
    public List<AccountBean> selectAccount(int page, String searchInfo, boolean isOrderByCreateTime, SQLiteDatabase db) {
        String orderBy = DBConstant.updateTime + " desc";
        if (isOrderByCreateTime) {
            orderBy = DBConstant.creatTime + " desc";
        }
        StringBuffer searchSql=null;
        String[]searchStr=new String[4];
        if(!TextUtils.isEmpty(searchInfo)){
            searchSql=new StringBuffer();
            searchSql.append(DBConstant.dataSource+" like ? or ");
            searchSql.append(DBConstant.dataAccount+" like ? or ");
            searchSql.append(DBConstant.dataPassword+" like ? or ");
            searchSql.append(DBConstant.dataRemark+" like ? ");
            searchStr[0]=searchInfo;
            searchStr[1]=searchInfo;
            searchStr[2]=searchInfo;
            searchStr[3]=searchInfo;
        }
        Cursor query = db.query(T_Account_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataSource,
                        DBConstant.dataAccount,
                        DBConstant.dataPassword,
                        DBConstant.dataRemark,
                        DBConstant.updateTime,
                        DBConstant.creatTime}, searchSql!=null?searchSql.toString():null,searchSql!=null?searchStr:null, null, null, orderBy,getLimit(page));
        List<AccountBean> list = new ArrayList<AccountBean>();
        AccountBean bean;
        while (query.moveToNext()) {
            bean = new AccountBean();
            int id = query.getInt(query.getColumnIndex(DBConstant._id));
            String dataSource = query.getString(query.getColumnIndex(DBConstant.dataSource));
            String dataAccount = query.getString(query.getColumnIndex(DBConstant.dataAccount));
            String dataPassword = query.getString(query.getColumnIndex(DBConstant.dataPassword));
            String dataRemark = query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime = query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime = query.getString(query.getColumnIndex(DBConstant.creatTime));
            bean.set_id(id);
            bean.setDataSource(dataSource);
            bean.setDataAccount(dataAccount);
            bean.setDataPassword(dataPassword);
            bean.setDataRemark(dataRemark);
            bean.setUpdateTime(DateUtils.stringToDate(updateTime, DateUtils.ymdhms));
            bean.setCreatTime(DateUtils.stringToDate(creatTime, DateUtils.ymdhms));

            list.add(bean);
        }
        db.close();
        return list;
    }
    @Override
    public boolean addOrEditAccount(AccountBean bean) {
        if (bean.get_id() == -1) {
            return addAccount(bean)>0;
        } else {
            return updateAccount(bean)>0;
        }
    }
    public long updateAccount(AccountBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.dataSource, bean.getDataSource());
        values.put(DBConstant.dataAccount, bean.getDataAccount());
        values.put(DBConstant.dataPassword, bean.getDataPassword());
        values.put(DBConstant.dataRemark, bean.getDataRemark());
        values.put(DBConstant.updateTime, DateUtils.getLocalDate());
        long insert = db.update(T_Account_Note, values, DBConstant._id + "=?", new String[]{bean.get_id() + ""});
        Log.i(TAG,insert+"");
        db.close();
        return insert;
    }

    public long addAccount(AccountBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.dataSource, bean.getDataSource());
        values.put(DBConstant.dataAccount, bean.getDataAccount());
        values.put(DBConstant.dataPassword, bean.getDataPassword());
        values.put(DBConstant.dataRemark, bean.getDataRemark());
        if (bean.getCreatTime() != null) {
            values.put(DBConstant.creatTime, DateUtils.dateToString(bean.getCreatTime(), DateUtils.ymdhms));
        }
        if (bean.getUpdateTime() != null) {
            values.put(DBConstant.updateTime, DateUtils.dateToString(bean.getUpdateTime(), DateUtils.ymdhms));
        }
        long insert = db.insert(T_Account_Note, null, values);
        Log.i(TAG,insert+"");
        db.close();
        return insert;
    }
}
