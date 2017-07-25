package com.newnote.database.dao.imp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.github.androidtools.DateUtils;
import com.newnote.base.BaseDao;
import com.newnote.database.DBConstant;
import com.newnote.database.DBManager;
import com.newnote.database.dao.AccountDao;
import com.newnote.module.account.entity.AccountBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */
public class AccountImp extends BaseDao implements AccountDao {
    public AccountImp(Context context) {
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
        Cursor query = db.query(DBManager.T_Account_Note,
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
}
