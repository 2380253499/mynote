package com.mynote.module.spend.dao.imp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import android.util.Log;

import com.github.androidtools.StringUtils;
import com.mynote.base.BaseDaoImp;
import com.mynote.database.DBConstant;
import com.mynote.database.DBManager;
import com.mynote.module.spend.bean.SpendBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/25.
 */

public class SpendImp extends BaseDaoImp {
    /**
     * 按年份分组
     *
     * @return
     */
    public List<SpendBean> selectSpendGroupByYear() {
        List<SpendBean> sbList = new ArrayList<SpendBean>();
        SpendBean bean;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select a." + DBConstant.localYear + ",sum(a." + DBConstant.liveSpend + ") as " + DBConstant.totalSpend + " from " + DBManager.T_Spend_Note + " as a " +
                " group by  " + DBConstant.localYear + " order by  " + DBConstant.localYear + " desc";
        Log.i("---", "---" + sql);
        Cursor query = db.rawQuery(sql, null);
        while (query.moveToNext()) {
            bean = new SpendBean();
            int localYear = query.getInt(query.getColumnIndex(DBConstant.localYear));
            Double totalSpend = query.getDouble(query.getColumnIndex(DBConstant.totalSpend));
            bean.setTotalSpend(StringUtils.keepDecimal(totalSpend));
            bean.setLocalYear(localYear);
            sbList.add(bean);
        }
        db.close();
        formatList(sbList);
        return sbList;
    }

    /**
     * 按月份分组
     *
     * @return
     */
    public List<SpendBean> selectSpendGroupByMonth(int year) {
        List<SpendBean> sbList = new ArrayList<SpendBean>();
        SpendBean bean;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select a." + DBConstant.localYear + ",a." + DBConstant.localMonth + ",sum(a." + DBConstant.liveSpend + ") as " + DBConstant.totalSpend + " from " + DBManager.T_Spend_Note + " as a " +
                " where " + DBConstant.localYear + "=? group by  " + DBConstant.localMonth + " order by  " + DBConstant.localMonth + " desc";
        Log.i("---", "---" + sql);
        Cursor query = db.rawQuery(sql, new String[]{year + ""});
        while (query.moveToNext()) {
            bean = new SpendBean();
            int localYear = query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth = query.getInt(query.getColumnIndex(DBConstant.localMonth));
            Double totalSpend = query.getDouble(query.getColumnIndex(DBConstant.totalSpend));
            bean.setTotalSpend(StringUtils.keepDecimal(totalSpend));
            bean.setLocalYear(localYear);
            bean.setLocalMonth(localMonth);
            sbList.add(bean);
        }
        db.close();
        formatList(sbList);
        return sbList;
    }

    /**
     * 按天分组
     *
     * @return
     */
    public List<SpendBean> selectSpendGroupByDay(int year, int month) {
        List<SpendBean> sbList = new ArrayList<SpendBean>();
        SpendBean bean;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select a." + DBConstant.localYear + ",a." + DBConstant.localMonth + ",a." + DBConstant.localDay + ",sum(a." + DBConstant.liveSpend + ") as " + DBConstant.totalSpend + " from " + DBManager.T_Spend_Note + " as a " +
                " where " + DBConstant.localYear + "=? and " + DBConstant.localMonth + "=? group by  " + DBConstant.localDay + " order by  " + DBConstant.localDay + " desc";
        Log.i("---", "---" + sql);
        Cursor query = db.rawQuery(sql, new String[]{year + "", month + ""});
        while (query.moveToNext()) {
            bean = new SpendBean();
            int localYear = query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth = query.getInt(query.getColumnIndex(DBConstant.localMonth));
            int localDay = query.getInt(query.getColumnIndex(DBConstant.localDay));
            Double totalSpend = query.getDouble(query.getColumnIndex(DBConstant.totalSpend));
            bean.setTotalSpend(StringUtils.keepDecimal(totalSpend));
            bean.setLocalYear(localYear);
            bean.setLocalMonth(localMonth);
            bean.setLocalDay(localDay);
            sbList.add(bean);
        }
        db.close();
        formatList(sbList);
        return sbList;
    }

    /**
     * 获取一天的消费记录
     *
     * @param year
     * @param month
     * @param day
     * @return
     */
    public List<SpendBean> selectSpendByOneDay(int year, int month, int day) {
        List<SpendBean> sbList = new ArrayList<SpendBean>();
        SpendBean bean;
        SQLiteDatabase db = getWritableDatabase();
        String sql = "select * from " + DBManager.T_Spend_Note +
                " where " + DBConstant.localYear + "=? and " + DBConstant.localMonth + "=? and " + DBConstant.localDay + "=? order by  " + DBConstant.creatTime + " desc";
        Log.i("---", "---" + sql);
        Cursor query = db.rawQuery(sql, new String[]{year + "", month + "", day + ""});
        while (query.moveToNext()) {
            bean = new SpendBean();
            int id = query.getInt(query.getColumnIndex(DBConstant._id));
            Double liveSpend = query.getDouble(query.getColumnIndex(DBConstant.liveSpend));
            String liveSpendStr = query.getString(query.getColumnIndex(DBConstant.liveSpend));
            String dataRemark = query.getString(query.getColumnIndex(DBConstant.dataRemark));
            long updateTime = query.getLong(query.getColumnIndex(DBConstant.updateTime));
            long creatTime = query.getLong(query.getColumnIndex(DBConstant.creatTime));
            int localYear = query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth = query.getInt(query.getColumnIndex(DBConstant.localMonth));
            int localDay = query.getInt(query.getColumnIndex(DBConstant.localDay));
            bean.set_id(id);
            bean.setLiveSpend(liveSpend);
            bean.setDataRemark( dataRemark) ;
            bean.setUpdateTime( updateTime );
            bean.setCreatTime( creatTime );
            bean.setLocalYear(localYear);
            bean.setLocalMonth(localMonth);
            bean.setLocalDay(localDay);
            sbList.add(bean);
        }
        db.close();
        formatList(sbList);
        return sbList;
    }
    /**
     * 删除消费
     * @param id
     * @return
     */
    public boolean deleteSpend(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(DBManager.T_Spend_Note, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete > 0 ? true : false;
    }
    public boolean deleteSpend(List<String> idList) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i = 0; i < idList.size(); i++) {
                db.delete(DBManager.T_Spend_Note, DBConstant._id + "=?", new String[]{idList.get(i)});
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
    public int selectSpendCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(0) as num from " + DBManager.T_Spend_Note, null);
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

    /***
     * 查询账户
     *
     * @return
     */
    public List<SpendBean> selectSpend(int page) {
        return selectSpend(page,false);
    }

    /***
     * 查询账户
     *
     * @param isOrderByCreateTime 是否按照创建时间排序true  按照修改修改时间排序false
     * @param db
     * @return
     */
    public List<SpendBean> selectSpend(int page,boolean isOrderByCreateTime, SQLiteDatabase db) {
        String orderBy = DBConstant.updateTime + " desc";
        if (isOrderByCreateTime) {
            orderBy = DBConstant.creatTime + " desc";
        }
        Cursor query = db.query(DBManager.T_Spend_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.uid,
                        DBConstant.dataRemark,
                        DBConstant.liveSpend,
                        DBConstant.localYear,
                        DBConstant.localMonth,
                        DBConstant.localDay,
                        DBConstant.updateTime,
                        DBConstant.creatTime}, null, null, null, null, orderBy,getLimit(page));
        List<SpendBean> list = new ArrayList<SpendBean>();
        SpendBean bean;
        while (query.moveToNext()) {
            bean = new SpendBean();
            int id = query.getInt(query.getColumnIndex(DBConstant._id));
            String uid = query.getString(query.getColumnIndex(DBConstant.uid));
            Double liveSpend = query.getDouble(query.getColumnIndex(DBConstant.liveSpend));
            String dataRemark = query.getString(query.getColumnIndex(DBConstant.dataRemark));
            int localYear = query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth = query.getInt(query.getColumnIndex(DBConstant.localMonth));
            int localDay = query.getInt(query.getColumnIndex(DBConstant.localDay));
            long updateTime = query.getLong(query.getColumnIndex(DBConstant.updateTime));
            long creatTime = query.getLong(query.getColumnIndex(DBConstant.creatTime));
            bean.set_id(id);
            bean.setUid(uid);
            bean.setLiveSpend(liveSpend);
            bean.setDataRemark(dataRemark);
            bean.setLocalYear(localYear);
            bean.setLocalMonth(localMonth);
            bean.setLocalDay(localDay);
            bean.setUpdateTime( updateTime );
            bean.setCreatTime( creatTime );
            list.add(bean);
        }
        db.close();
        formatList(list);
        return list;
    }

    /***
     * 查询账户
     *
     * @param isOrderByCreateTime 是否按照创建时间排序true  按照修改修改时间排序false
     * @return
     */
    public List<SpendBean> selectSpend(int page,  boolean isOrderByCreateTime) {
        return selectSpend(page, isOrderByCreateTime, getWritableDatabase());
    }
    public long addOrEditSpend(SpendBean bean) {
        if (bean.get_id() == -1) {
            return addSpend(bean);
        } else {
            return updateSpend(bean);
        }
    }

    public long updateSpend(SpendBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.dataRemark,bean.getDataRemark());
        values.put(DBConstant.liveSpend, bean.getLiveSpend());
        values.put(DBConstant.updateTime, System.currentTimeMillis());
        long insert = db.update(DBManager.T_Spend_Note, values, DBConstant._id + "=?", new String[]{bean.get_id() + ""});
        db.close();
        return insert;
    }

    public long addSpend(SpendBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.dataRemark,bean.getDataRemark());
        values.put(DBConstant.liveSpend, bean.getLiveSpend());
        if(TextUtils.isEmpty(bean.getUid())||"-1".equals(bean.getUid())){
            bean.setUid(System.nanoTime()+"");
            values.put(DBConstant.uid, bean.getUid());
        }
        if (bean.getLocalYear() > 0) {
            values.put(DBConstant.localYear, bean.getLocalYear());
            values.put(DBConstant.localMonth, bean.getLocalMonth());
            values.put(DBConstant.localDay, bean.getLocalDay());
        }
        if (bean.getCreatTime() != 0) {
            values.put(DBConstant.creatTime, bean.getCreatTime() );
        }else{
            values.put(DBConstant.creatTime, new Date().getTime() );
        }
        if (bean.getUpdateTime() != 0) {
            values.put(DBConstant.updateTime, bean.getUpdateTime());
        }else{
            values.put(DBConstant.updateTime,new Date().getTime());
        }
        long insert = db.insert(DBManager.T_Spend_Note, null, values);
        db.close();
        return insert;
    }
}
