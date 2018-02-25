package com.mynote.module.memo.dao.imp;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.mynote.base.BaseDaoImp;
import com.mynote.database.DBConstant;
import com.mynote.database.DBManager;
import com.mynote.module.memo.bean.MemoBean;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2018/2/25.
 */

public class MemoImp extends BaseDaoImp {
    public boolean deleteMemo(int id) {
        SQLiteDatabase db = getWritableDatabase();
        int delete = db.delete(DBManager.T_Memo_Note, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete > 0 ? true : false;
    }

    public boolean deleteMemo(List<String> idList) {
        SQLiteDatabase db = getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i = 0; i < idList.size(); i++) {
                db.delete(DBManager.T_Memo_Note, DBConstant._id + "=?", new String[]{idList.get(i)});
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
    public int selectMemoCount() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(0) as num from " + DBManager.T_Memo_Note, null);
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
    public List<MemoBean> selectMemo(int page) {
        return selectMemo(page,null, false);
    }

    /***
     * 查询账户
     *
     * @param searchInfo          模糊搜索关键字
     * @param isOrderByCreateTime 是否按照创建时间排序true  按照修改修改时间排序false
     * @param db
     * @return
     */
    public List<MemoBean> selectMemo(int page,String searchInfo, boolean isOrderByCreateTime, SQLiteDatabase db) {
        String orderBy = DBConstant.updateTime + " desc";
        if (isOrderByCreateTime) {
            orderBy = DBConstant.creatTime + " desc";
        }
        StringBuffer searchSql=null;
        String[]searchStr=new String[2];
        if(!TextUtils.isEmpty(searchInfo)){
            searchSql=new StringBuffer();
            searchSql.append(DBConstant.dataRemark+" like ? or ");
            searchSql.append(DBConstant.dataContent+" like ? or ");
            searchStr[0]="%"+searchInfo+"%";
            searchStr[1]="%"+searchInfo+"%";
        }
        Cursor query = db.query(DBManager.T_Memo_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.uid,
                        DBConstant.dataRemark,
                        DBConstant.dataContent,
                        DBConstant.updateTime,
                        DBConstant.creatTime}, searchSql!=null?searchSql.toString():null,searchSql!=null?searchStr:null, null, null, orderBy,getLimit(page));
        List<MemoBean> list = new ArrayList<MemoBean>();
        MemoBean bean;
        while (query.moveToNext()) {
            bean = new MemoBean();
            int id = query.getInt(query.getColumnIndex(DBConstant._id));
            String uid = query.getString(query.getColumnIndex(DBConstant.uid));
            String dataRemark = query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String dataContent = query.getString(query.getColumnIndex(DBConstant.dataContent));
            long updateTime = query.getLong(query.getColumnIndex(DBConstant.updateTime));
            long creatTime = query.getLong(query.getColumnIndex(DBConstant.creatTime));
            bean.set_id(id);
            bean.setUid(uid);
            bean.setDataContent(dataContent);
            bean.setDataRemark(dataRemark);
            bean.setUpdateTime(updateTime);
            bean.setCreatTime(creatTime);
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
    public List<MemoBean> selectMemo(int page,String searchInfo, boolean isOrderByCreateTime) {
        return selectMemo(page,searchInfo, isOrderByCreateTime, getWritableDatabase());
    }

    public long addOrEditMemo(MemoBean bean) {
        if (bean.get_id() == -1) {
            return addMemo(bean);
        } else {
            return updateMemo(bean);
        }
    }

    public long updateMemo(MemoBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.dataRemark,  bean.getDataRemark()) ;
        values.put(DBConstant.dataContent,  bean.getDataContent() );
        values.put(DBConstant.updateTime, System.currentTimeMillis());
        long insert = db.update(DBManager.T_Memo_Note, values, DBConstant._id + "=?", new String[]{bean.get_id() + ""});
        db.close();
        return insert;
    }

    public long addMemo(MemoBean bean) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DBConstant.dataRemark,  bean.getDataRemark()) ;
        values.put(DBConstant.dataContent,  bean.getDataContent() );
        if(TextUtils.isEmpty(bean.getUid())||"-1".equals(bean.getUid())){
            bean.setUid(System.nanoTime()+"");
            values.put(DBConstant.uid, bean.getUid());
        }
        if (bean.getCreatTime() != 0) {
            values.put(DBConstant.creatTime, bean.getCreatTime() );
        }else{
            values.put(DBConstant.creatTime, new Date().getTime() );
        }
        if (bean.getUpdateTime() != 0) {
            values.put(DBConstant.updateTime,bean.getUpdateTime() );
        }else{
            values.put(DBConstant.updateTime,new Date().getTime());
        }
        long insert = db.insert(DBManager.T_Memo_Note, null, values);
        db.close();
        return insert;
    }
}
