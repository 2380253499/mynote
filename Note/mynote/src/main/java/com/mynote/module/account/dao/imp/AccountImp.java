package com.mynote.module.account.dao.imp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.mynote.base.BaseDaoImp;
import com.mynote.database.DBConstant;
import com.mynote.database.DBManager;
import com.mynote.module.account.bean.AccountBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/1/25.
 */

public class AccountImp extends BaseDaoImp{
    public boolean deleteAccount(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(DBManager.T_Account_Note, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete > 0 ? true : false;
    }

    public boolean deleteAccount(List<String> idList) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i = 0; i < idList.size(); i++) {
                db.delete(DBManager.T_Account_Note, DBConstant._id + "=?", new String[]{idList.get(i)});
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
            db.close();
        }
        return true;
    }
    public int selectAccountCount(String uid) {
        String sql="select count(0) as num from " + DBManager.T_Account_Note;
        if(!TextUtils.isEmpty(uid)&&uid.trim().length()>0){
            sql="select count(0) as num from " + DBManager.T_Account_Note+" where "+DBConstant.uid+"=?";
        }
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(sql,uid==null?null:new String[]{uid});
        int count = 0;
        try {
            while (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("num"));
            }
            cursor.close();
            db.close();
        } catch (Exception e) {
            cursor.close();
            db.close();
            return -1;
        }
        return count;
    }
    public int selectAccountCount() {
        return selectAccountCount(null);
    }

    /***
     * 查询账户
     *
     * @return
     */
    public List<AccountBean> selectAccount(int page) {
        return selectAccount(page,null, false);
    }

    /***
     * 查询账户
     *
     * @param searchInfo          模糊搜索关键字
     * @param isOrderByCreateTime 是否按照创建时间排序true  按照修改修改时间排序false
     * @param db
     * @return
     */
    public List<AccountBean> selectAccount(int page,String searchInfo, boolean isOrderByCreateTime, SQLiteDatabase db) {
        String orderBy = DBConstant.updateTime + " desc";
        if (isOrderByCreateTime) {
            orderBy = DBConstant.createTime + " desc";
        }
        StringBuffer searchSql=null;
        String[]searchStr=new String[4];
        if(!TextUtils.isEmpty(searchInfo)){
            searchSql=new StringBuffer();
            searchSql.append(DBConstant.dataSource+" like ? or ");
            searchSql.append(DBConstant.dataAccount+" like ? or ");
            searchSql.append(DBConstant.dataPassword+" like ? or ");
            searchSql.append(DBConstant.dataRemark+" like ? ");
            searchStr[0]="%"+searchInfo+"%";
            searchStr[1]="%"+searchInfo+"%";
            searchStr[2]="%"+searchInfo+"%";
            searchStr[3]="%"+searchInfo+"%";
        }
        Cursor query = db.query(DBManager.T_Account_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.uid,
                        DBConstant.dataSource,
                        DBConstant.dataAccount,
                        DBConstant.dataPassword,
                        DBConstant.dataRemark,
                        DBConstant.updateTime,
                        DBConstant.createTime}, searchSql!=null?searchSql.toString():null,searchSql!=null?searchStr:null, null, null, orderBy,getLimit(page));
        List<AccountBean> list = new ArrayList<AccountBean>();
        AccountBean bean;
        while (query.moveToNext()) {
            bean = new AccountBean();
            int id = query.getInt(query.getColumnIndex(DBConstant._id));
            String uid = query.getString(query.getColumnIndex(DBConstant.uid));
            String dataSource = query.getString(query.getColumnIndex(DBConstant.dataSource));
            String dataAccount = query.getString(query.getColumnIndex(DBConstant.dataAccount));
            String dataPassword = query.getString(query.getColumnIndex(DBConstant.dataPassword));
            String dataRemark = query.getString(query.getColumnIndex(DBConstant.dataRemark));
            long updateTime = query.getLong(query.getColumnIndex(DBConstant.updateTime));
            long createTime = query.getLong(query.getColumnIndex(DBConstant.createTime));

//            long updateTime = string2Date(query.getString(query.getColumnIndex(DBConstant.updateTime)));
//            long creatTime = string2Date(query.getString(query.getColumnIndex(DBConstant.createTime)));
            bean.set_id(id);
            bean.setUid(uid);
            bean.setDataSource(dataSource);
            bean.setDataAccount(dataAccount);
            bean.setDataPassword(dataPassword);
            bean.setDataRemark(dataRemark);
            bean.setUpdateTime(updateTime);
            bean.setCreateTime(createTime);
            list.add(bean);
        }
        db.close();
        formatList(list);
        return list;
    }

    /***
     * 查询账户
     *
     * @param searchInfo          模糊搜索关键字
     * @param isOrderByCreateTime 是否按照创建时间排序true  按照修改修改时间排序false
     * @return
     */
    public List<AccountBean> selectAccount(int page,String searchInfo, boolean isOrderByCreateTime) {
        return selectAccount(page,searchInfo, isOrderByCreateTime, getWritableDatabase());
    }

    public long addOrEditAccount(AccountBean bean) {
        if (bean.get_id() == -1) {
            return addAccount(bean);
        } else {
            return updateAccount(bean);
        }
    }

    public long updateAccount(AccountBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.dataSource,  bean.getDataSource()) ;
        values.put(DBConstant.dataAccount,  bean.getDataAccount() );
        values.put(DBConstant.dataPassword,  bean.getDataPassword() );
        values.put(DBConstant.dataRemark,  bean.getDataRemark());
        values.put(DBConstant.updateTime, System.currentTimeMillis());
        long insert = db.update(DBManager.T_Account_Note, values, DBConstant._id + "=?", new String[]{bean.get_id() + ""});
        db.close();
        return insert;
    }

    public long addAccount(AccountBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.dataSource,bean.getDataSource());
        values.put(DBConstant.dataAccount,bean.getDataAccount());
        values.put(DBConstant.dataPassword,bean.getDataPassword());
        values.put(DBConstant.dataRemark,bean.getDataRemark());
        if(TextUtils.isEmpty(bean.getUid())||"-1".equals(bean.getUid())){
            bean.setUid(System.nanoTime()+"");
        }
        values.put(DBConstant.uid, bean.getUid());

        if (bean.getCreateTime() != 0) {
            values.put(DBConstant.createTime, bean.getCreateTime() );
        }else{
            values.put(DBConstant.createTime, new Date().getTime() );
        }
        if (bean.getUpdateTime() != 0) {
            values.put(DBConstant.updateTime,bean.getUpdateTime() );
        }else{
            values.put(DBConstant.updateTime,new Date().getTime());
        }
        long insert = db.insert(DBManager.T_Account_Note, null, values);
        db.close();
        return insert;
    }

}
