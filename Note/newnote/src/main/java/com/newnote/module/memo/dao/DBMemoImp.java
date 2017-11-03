package com.newnote.module.memo.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.github.androidtools.DateUtils;
import com.newnote.base.BaseDao;
import com.newnote.database.DBConstant;
import com.newnote.database.DBManager;
import com.newnote.module.memo.entity.MemoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/25.
 */
public class DBMemoImp extends BaseDao implements DBMemo {
    public DBMemoImp(Context context) {
        super(context);
    }
    @Override
    public List<MemoBean> selectMemo(int page) {
        return selectMemo(page,null, true);
    }
    @Override
    public List<MemoBean> selectMemo(int page, String searchInfo, boolean isOrderByCreateTime) {
        return selectMemo(page,searchInfo, isOrderByCreateTime,getWritableDatabase());
    }
    @Override
    public List<MemoBean> selectMemo(int page, String searchInfo, boolean isOrderByCreateTime, SQLiteDatabase db) {
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
            searchStr[0]=searchInfo;
            searchStr[1]=searchInfo;
        }
        Cursor query = db.query(DBManager.T_Memo_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataRemark,
                        DBConstant.dataContent,
                        DBConstant.updateTime,
                        DBConstant.creatTime}, searchSql!=null?searchSql.toString():null,searchSql!=null?searchStr:null, null, null, orderBy,getLimit(page));
        List<MemoBean> list = new ArrayList<MemoBean>();
        MemoBean bean;
        while (query.moveToNext()) {
            bean = new MemoBean();
            int id = query.getInt(query.getColumnIndex(DBConstant._id));
            String dataMemo = query.getString(query.getColumnIndex(DBConstant.dataContent));
            String dataRemark = query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime = query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime = query.getString(query.getColumnIndex(DBConstant.creatTime));
            bean.set_id(id);
            bean.setDataContent(dataMemo);
            bean.setDataRemark(dataRemark);
            bean.setUpdateTime(DateUtils.stringToDate(updateTime, DateUtils.ymdhms));
            bean.setCreatTime(DateUtils.stringToDate(creatTime, DateUtils.ymdhms));

            list.add(bean);
        }
        db.close();
        return list;
    }
}
