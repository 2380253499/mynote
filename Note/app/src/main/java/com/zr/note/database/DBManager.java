package com.zr.note.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.util.SparseArray;

import com.zr.note.tools.AES;
import com.zr.note.tools.DateUtils;
import com.zr.note.tools.LogUtils;
import com.zr.note.tools.StringUtils;
import com.zr.note.ui.main.entity.AccountBean;
import com.zr.note.ui.main.entity.JokeBean;
import com.zr.note.ui.main.entity.MemoBean;
import com.zr.note.ui.main.entity.SpendBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/10/11.
 */
public class DBManager extends SQLiteOpenHelper{
    /*
    * version 3--消费表增加year month day三个varchar字段
    * version 4--消费表增加year month day三个varchar字段修改为integer类型
    * */
    private static final String dbName="MyNote";
    public String getDBName(){
        return dbName;
    }
    private static final int version=4;
    private static DBManager dbManager;
    public static final String T_Account_Note="T_Account_Note";
    public static final String T_Memo_Note="T_Memo_Note";
    public static final String T_Joke_Note="T_Joke_Note";
    public static final String T_Spend_Note="T_Spend_Note";
    private DBManager(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }
    public static DBManager getInstance(Context context) {
        if(dbManager==null){
            synchronized (DBManager.class){
                if (dbManager == null) {
                    dbManager = new DBManager(context,dbName,null,version);
                }
            }
        }
        return dbManager;
    }

    public static DBManager getNewInstance(Context context) {
        return new DBManager(context,dbName,null,version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        addDataTable(db);
    }
    private void addDataTable(SQLiteDatabase db,String table) {
        if (noExistTable(db,table)) {
            db.execSQL(DBConstant.CT_Spend_Note);
        }
    }
    private void addDataTable(SQLiteDatabase db) {
        if (noExistTable(db,T_Account_Note)) {
            db.execSQL(DBConstant.CT_Account_Note);
        }
        if (noExistTable(db,T_Memo_Note)) {
            db.execSQL(DBConstant.CT_Memo_Note);
        }
        if (noExistTable(db,T_Joke_Note)) {
            db.execSQL(DBConstant.CT_Joke_Note);
        }
        if (noExistTable(db,T_Spend_Note)) {
            db.execSQL(DBConstant.CT_Spend_Note);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch (oldVersion){
            case 1:
                if(existTable(db,T_Spend_Note)){
                    dropTable(db,T_Spend_Note);
                    addDataTable(db,DBConstant.CT_Spend_Note);
                }
            break;
            case 2:
                if(existTable(db,T_Spend_Note)){
                    dropTable(db,T_Spend_Note);
                    addDataTable(db,DBConstant.CT_Spend_Note);
                }
            break;
            case 3:
                if(existTable(db,T_Spend_Note)){
                    dropTable(db,T_Spend_Note);
                    addDataTable(db,DBConstant.CT_Spend_Note);
                }
            break;
        }
    }
    private boolean dropTable(SQLiteDatabase db,String table){
        try{
            db.execSQL("drop table "+table);
        }catch (Exception e){
            return false;
        }
        return true;
    }
    private boolean existTable(SQLiteDatabase db,String table){
        boolean exits = false;
        String sql = "select * from sqlite_master where name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{table});
        if(cursor.getCount()!=0){
            exits = true;
        }
        cursor.close();
        return exits;
    }
    private boolean noExistTable(SQLiteDatabase db,String table){
        boolean exits = true;
        String sql = "select * from sqlite_master where name=?";
        Cursor cursor = db.rawQuery(sql, new String[]{table});
        if(cursor.getCount()!=0){
            exits = false;
        }
        cursor.close();
        return exits;
    }
    /**************************************操作数据方法************************************************/
    public boolean deleteAccount(int id){
        SQLiteDatabase db=getWritableDatabase();
        int delete = db.delete(T_Account_Note, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete>0?true:false;
    }
    public boolean deleteAccount(String[] id){
        SQLiteDatabase db=getWritableDatabase();
        try {
            db.beginTransaction();
            for (int i = 0; i < id.length; i++) {
                db.delete(T_Account_Note, DBConstant._id + "=?", new String[]{id[i]});
            }
            db.setTransactionSuccessful();
        }catch (Exception e){
            LogUtils.Log("批量删除异常:"+e);
            return false;
        }finally {
            db.endTransaction();
            db.close();
        }
        return true;
    }
    public int selectAccountCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(0) as num from " + T_Account_Note, null);
        int count = 0;
        try{
            while (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("num"));
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            cursor.close();
            db.close();
            return -1;
        }
        return count;
    }
    /***
     * 查询账户
     * @return
     */
    public List<AccountBean> selectAccount(){
        return selectAccount(null,true);
    }
    /***
     * 查询账户
     * @param searchInfo  模糊搜索关键字
     * @param isOrderByCreateTime  是否按照创建时间排序true  按照修改修改时间排序false
     * @param db
     * @return
     */
    public List<AccountBean> selectAccount(String searchInfo,boolean isOrderByCreateTime,SQLiteDatabase db){
        String orderBy=DBConstant.updateTime+" desc";
        if(isOrderByCreateTime){
            orderBy = DBConstant.creatTime + " desc";
        }
        Cursor query = db.query(T_Account_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataSource,
                        DBConstant.dataAccount,
                        DBConstant.dataPassword,
                        DBConstant.dataRemark,
                        DBConstant.updateTime,
                        DBConstant.creatTime}, null, null, null, null, orderBy);
        List<AccountBean>list=new ArrayList<AccountBean>();
        AccountBean bean;
        while (query.moveToNext()){
            bean=new AccountBean();
            int id=query.getInt(query.getColumnIndex(DBConstant._id));
            String dataSource=query.getString(query.getColumnIndex(DBConstant.dataSource));
            String dataAccount=query.getString(query.getColumnIndex(DBConstant.dataAccount));
            String dataPassword=query.getString(query.getColumnIndex(DBConstant.dataPassword));
            String dataRemark=query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime=query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime=query.getString(query.getColumnIndex(DBConstant.creatTime));
            bean.set_id(id);
            bean.setDataSource(AES.decode(dataSource));
            bean.setDataAccount(AES.decode(dataAccount));
            bean.setDataPassword(AES.decode(dataPassword));
            bean.setDataRemark(AES.decode(dataRemark));
            bean.setUpdateTime(DateUtils.stringToDate(updateTime, DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime, DateUtils.ymdhm));
            if(searchInfo!=null){
                if(bean.getDataAccount().toLowerCase().indexOf(searchInfo.toLowerCase())>=0
                        ||bean.getDataSource().toLowerCase().indexOf(searchInfo.toLowerCase())>=0
                        ||bean.getDataRemark().toLowerCase().indexOf(searchInfo.toLowerCase())>=0){
                    list.add(bean);
                }
            }else{
                list.add(bean);
            }
        }
        db.close();
        return list;
    }
    /***
     * 查询账户
     * @param searchInfo  模糊搜索关键字
     * @param isOrderByCreateTime  是否按照创建时间排序true  按照修改修改时间排序false
     * @return
     */
    public List<AccountBean> selectAccount(String searchInfo,boolean isOrderByCreateTime){
        return selectAccount(searchInfo,isOrderByCreateTime,getWritableDatabase());
    }
    public long addOrEditAccount(AccountBean bean){
        if(bean.get_id()==-1){
            return addAccount(bean);
        }else{
            return updateAccount(bean);
        }

    }
    public long updateAccount(AccountBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataSource, AES.encode(bean.getDataSource()));
        values.put(DBConstant.dataAccount,AES.encode(bean.getDataAccount()));
        values.put(DBConstant.dataPassword,AES.encode(bean.getDataPassword()));
        values.put(DBConstant.dataRemark, AES.encode(bean.getDataRemark()));
        values.put(DBConstant.updateTime,DateUtils.getLocalDate());
        long insert = db.update(T_Account_Note, values, DBConstant._id + "=?", new String[]{bean.get_id() + ""});
        LogUtils.Log(insert);
        db.close();
        return insert;
    }
    public long addAccount(AccountBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataSource, AES.encode(bean.getDataSource()));
        values.put(DBConstant.dataAccount,AES.encode(bean.getDataAccount()));
        values.put(DBConstant.dataPassword,AES.encode(bean.getDataPassword()));
        values.put(DBConstant.dataRemark, AES.encode(bean.getDataRemark()));
        if(bean.getCreatTime()!=null){
            values.put(DBConstant.creatTime, DateUtils.dateToString(bean.getCreatTime(),DateUtils.ymdhm));
        }
        if(bean.getUpdateTime()!=null){
            values.put(DBConstant.updateTime, DateUtils.dateToString(bean.getUpdateTime(),DateUtils.ymdhm));
        }
        long insert = db.insert(T_Account_Note, null, values);
        LogUtils.Log(insert);
        db.close();
        return insert;
    }
    public long addOrEditMemo(MemoBean bean){
        if (bean.get_id() == -1) {
            return addMemo(bean);
        }else{
            return updateMemo(bean);
        }
    }
    public long updateMemo(MemoBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataRemark,AES.encode(bean.getDataRemark()));
        values.put(DBConstant.dataContent, AES.encode(bean.getDataContent()));
        values.put(DBConstant.updateTime,DateUtils.getLocalDate());
        long insert = db.update(T_Memo_Note, values, DBConstant._id + "=?", new String[]{bean.get_id() + ""});
        LogUtils.Log(insert);
        db.close();
        return insert;
    }

    /**
     * 添加备忘录
     * @param bean
     * @return
     */
    public long addMemo(MemoBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataRemark,AES.encode(bean.getDataRemark()));
        values.put(DBConstant.dataContent, AES.encode(bean.getDataContent()));
        if(bean.getCreatTime()!=null){
            values.put(DBConstant.creatTime, DateUtils.dateToString(bean.getCreatTime(),DateUtils.ymdhm));
        }
        if(bean.getUpdateTime()!=null){
            values.put(DBConstant.updateTime, DateUtils.dateToString(bean.getUpdateTime(),DateUtils.ymdhm));
        }
        long insert = db.insert(T_Memo_Note, null, values);
        LogUtils.Log(insert);
        db.close();
        return insert;
    }
    /***
     * 查询备忘录
     * @return
     */
    public List<MemoBean> selectMemo(){
        return selectMemo(null,true);
    }
    /***
     * 查询备忘录
     * @param searchInfo  模糊搜索关键字
     * @param isOrderByCreateTime  是否按照创建时间排序true  按照修改修改时间排序false
     * @return
     */
    public List<MemoBean> selectMemo(String searchInfo,boolean isOrderByCreateTime){
        return selectMemo(searchInfo,isOrderByCreateTime,getWritableDatabase());
    }
    /***
     * 查询备忘录
     * @param searchInfo  模糊搜索关键字
     * @param isOrderByCreateTime  是否按照创建时间排序true  按照修改修改时间排序false
     * @param db
     * @return
     */
    public List<MemoBean> selectMemo(String searchInfo,boolean isOrderByCreateTime,SQLiteDatabase db){
        String orderBy=DBConstant.updateTime+" desc";
        if(isOrderByCreateTime){
            orderBy=DBConstant.creatTime+" desc";
        }
        Cursor query = db.query(T_Memo_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataRemark,
                        DBConstant.dataContent,
                        DBConstant.updateTime,
                        DBConstant.creatTime}, null, null, null, null,orderBy);
        List<MemoBean>list=new ArrayList<MemoBean>();
        MemoBean bean;
        while (query.moveToNext()){
            bean=new MemoBean();
            int id=query.getInt(query.getColumnIndex(DBConstant._id));
            String dataContent=query.getString(query.getColumnIndex(DBConstant.dataContent));
            String dataRemark=query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime=query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime=query.getString(query.getColumnIndex(DBConstant.creatTime));
            bean.set_id(id);
            bean.setDataContent(AES.decode(dataContent));
            bean.setDataRemark(AES.decode(dataRemark));
            bean.setUpdateTime(DateUtils.stringToDate(updateTime, DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime, DateUtils.ymdhm));
            if(searchInfo!=null){
                if(bean.getDataContent().toLowerCase().indexOf(searchInfo.toLowerCase())>=0
                        ||bean.getDataRemark().toLowerCase().indexOf(searchInfo.toLowerCase())>=0){
                    list.add(bean);
                }
            }else{
                list.add(bean);
            }
        }
        db.close();
        return list;
    }

    /**
     * 删除备忘
     * @param id
     * @return
     */
    public boolean deleteMemo(int id){
        SQLiteDatabase db=getWritableDatabase();
        int delete = db.delete(T_Memo_Note, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete>0?true:false;
    }

    public long addOrEditJoke(JokeBean bean){
        if(bean.get_id()==-1){
            return addJoke(bean);
        }else{
            return updateJoke(bean);
        }
    }
    public long updateJoke(JokeBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataRemark, AES.encode(bean.getDataRemark()));
        values.put(DBConstant.dataContent, AES.encode(bean.getDataContent()));
        values.put(DBConstant.updateTime,DateUtils.getLocalDate());
        long insert = db.update(T_Joke_Note, values, DBConstant._id + "=?", new String[]{bean.get_id() + ""});
        LogUtils.Log(bean.get_id()+"==="+insert);
        db.close();
        return insert;
    }
    public long addJoke(JokeBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataRemark, AES.encode(bean.getDataRemark()));
        values.put(DBConstant.dataContent, AES.encode(bean.getDataContent()));
        if(bean.getCreatTime()!=null){
            values.put(DBConstant.creatTime, DateUtils.dateToString(bean.getCreatTime(),DateUtils.ymdhm));
        }
        if(bean.getUpdateTime()!=null){
            values.put(DBConstant.updateTime, DateUtils.dateToString(bean.getUpdateTime(),DateUtils.ymdhm));
        }
        long insert = db.insert(T_Joke_Note, null, values);
        LogUtils.Log(insert);
        db.close();
        return insert;
    }
    public int selectTableCount(String tableName){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(0) as num from "+tableName,null);
        int count = 0;
        try{
            while (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("num"));
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            cursor.close();
            db.close();
            return -1;
        }
        return count;
    }
    public int selectJokeCount(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select count(0) as num from "+T_Joke_Note,null);
        int count = 0;
        try{
            while (cursor.moveToNext()) {
                count = cursor.getInt(cursor.getColumnIndex("num"));
            }
            cursor.close();
            db.close();
        }catch (Exception e){
            cursor.close();
            db.close();
            return -1;
        }
        return count;
    }
    /***
     * 查询搞笑段子
     * @return
     */
    public List<JokeBean> selectJoke(){
        return selectJoke(null,true);
    }
    /***
     * 查询搞笑段子
     * @param searchInfo  模糊搜索关键字
     * @param isOrderByCreateTime  是否按照创建时间排序true  按照修改修改时间排序false
     * @return
     */
    public List<JokeBean> selectJoke(String searchInfo,boolean isOrderByCreateTime){
        return selectJoke(searchInfo,isOrderByCreateTime,getWritableDatabase());
    }
    /***
     * 查询搞笑段子
     * @param searchInfo  模糊搜索关键字
     * @param isOrderByCreateTime  是否按照创建时间排序true  按照修改修改时间排序false
     * @param db
     * @return
     */
    public List<JokeBean> selectJoke(String searchInfo,boolean isOrderByCreateTime,SQLiteDatabase db){
        String orderBy=DBConstant.updateTime+" desc";
        if(isOrderByCreateTime){
            orderBy=DBConstant.creatTime+" desc";
        }
        Cursor query = db.query(T_Joke_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataRemark,
                        DBConstant.dataContent,
                        DBConstant.updateTime,
                        DBConstant.creatTime}, null, null, null, null,orderBy);
        List<JokeBean>list=new ArrayList<JokeBean>();
        JokeBean bean;
        while (query.moveToNext()){
            bean=new JokeBean();
            int id=query.getInt(query.getColumnIndex(DBConstant._id));
            String dataRemark=query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String dataContent=query.getString(query.getColumnIndex(DBConstant.dataContent));
            String updateTime=query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime=query.getString(query.getColumnIndex(DBConstant.creatTime));
            bean.set_id(id);
            bean.setDataRemark(AES.decode(dataRemark));
            bean.setDataContent(AES.decode(dataContent));
            bean.setUpdateTime(DateUtils.stringToDate(updateTime, DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime, DateUtils.ymdhm));
            if(searchInfo!=null){
                if(bean.getDataContent().toLowerCase().indexOf(searchInfo.toLowerCase())>=0
                        ||bean.getDataRemark().toLowerCase().indexOf(searchInfo.toLowerCase())>=0){
                    list.add(bean);
                }
            }else{
                list.add(bean);
            }
        }
        db.close();
        return list;
    }
    /**
     * 删除joke
     * @param id
     * @return
     */
    public boolean deleteJoke(int id){
        SQLiteDatabase db=getWritableDatabase();
        int delete = db.delete(T_Joke_Note, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete>0?true:false;
    }
    /***
     * 查询生活消费
     * @return
     */
    public List<SpendBean> selectSpend(){
        return selectSpend(true);
    }
    /***
     * 查询生活消费
     * @param isOrderByCreateTime  是否按照创建时间排序true  按照修改修改时间排序false
     * @return
     */
    public List<SpendBean> selectSpend(boolean isOrderByCreateTime){
        return selectSpend(isOrderByCreateTime,getWritableDatabase());
    }
    /***
     * 查询生活消费
     * @param isOrderByCreateTime  是否按照创建时间排序true  按照修改修改时间排序false
     * @param db
     * @return
     */
    public List<SpendBean> selectSpend(boolean isOrderByCreateTime,SQLiteDatabase db){
        String orderBy=DBConstant.updateTime+" desc";
        if(isOrderByCreateTime){
            orderBy=DBConstant.creatTime+" desc";
        }
        Cursor query = db.query(T_Spend_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataRemark,
                        DBConstant.liveSpend,
                        DBConstant.updateTime,
                        DBConstant.creatTime}, null, null, null, null,orderBy);
        List<SpendBean>list=new ArrayList<SpendBean>();
        SpendBean bean;
        while (query.moveToNext()){
            bean=new SpendBean();
            int id=query.getInt(query.getColumnIndex(DBConstant._id));
            Double liveSpend=query.getDouble(query.getColumnIndex(DBConstant.liveSpend));
            String dataRemark=query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime=query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime=query.getString(query.getColumnIndex(DBConstant.creatTime));
            bean.set_id(id);
            bean.setLiveSpend(liveSpend);
            bean.setDataRemark(AES.decode(dataRemark));
            bean.setUpdateTime(DateUtils.stringToDate(updateTime,DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime,DateUtils.ymdhm));
            list.add(bean);
        }
        db.close();
        return list;
    }
    public long addOrEditSpend(SpendBean bean){
        if(bean.get_id()==-1){
            return addSpend(bean);
        }else {
            return updateSpend(bean);
        }
    }
    public long updateSpend(SpendBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataRemark, AES.encode(bean.getDataRemark()));
        values.put(DBConstant.liveSpend, bean.getLiveSpend());
        values.put(DBConstant.updateTime,DateUtils.getLocalDate());
        long insert = db.update(T_Spend_Note, values, DBConstant._id + "=?", new String[]{bean.get_id() + ""});
        LogUtils.Log(insert);
        db.close();
        return insert;
    }
    public long addSpend(SpendBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataRemark, AES.encode(bean.getDataRemark()));
        values.put(DBConstant.liveSpend, bean.getLiveSpend());
        if(bean.getLocalYear()>0){
            values.put(DBConstant.localYear, bean.getLocalYear());
            values.put(DBConstant.localMonth, bean.getLocalMonth());
            values.put(DBConstant.localDay, bean.getLocalDay());
        }
        if(bean.getCreatTime()!=null){
            values.put(DBConstant.creatTime, DateUtils.dateToString(bean.getCreatTime(),DateUtils.ymdhm));
        }
        if(bean.getUpdateTime()!=null){
            values.put(DBConstant.updateTime, DateUtils.dateToString(bean.getUpdateTime(), DateUtils.ymdhm));
        }
        long insert = db.insert(T_Spend_Note, null, values);
        LogUtils.Log(insert);
        db.close();
        return insert;
    }

    /**
     * 删除消费
     * @param id
     * @return
     */
    public boolean deleteSpend(int id){
        SQLiteDatabase db=getWritableDatabase();
        int delete = db.delete(T_Spend_Note, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete>0?true:false;
    }
    /**
     * 删除数据
     * @param table 表名
     * @param id    标识id
     * @return
     */
    public boolean deleteData(String table,int id){
        SQLiteDatabase db=getWritableDatabase();
        int delete = db.delete(table, DBConstant._id + "=?", new String[]{id + ""});
        db.close();
        return delete>0?true:false;
    }/**
     * 按年份分组
     * @return
     */
    public List<SpendBean> selectSpendGroupByYear(){
        List<SpendBean> sbList=new ArrayList<SpendBean>();
        SpendBean bean;
        SQLiteDatabase db=getWritableDatabase();
        String sql="select a."+DBConstant.localYear+",sum(a."+DBConstant.liveSpend+") as "+DBConstant.totalSpend+" from " + DBManager.T_Spend_Note + " as a " +
                " group by  "+DBConstant.localYear+" order by  "+DBConstant.localYear+" desc";
        Log.i("---","---"+sql);
        Cursor query = db.rawQuery(sql, null);
        while (query.moveToNext()){
            bean=new SpendBean();
            int localYear=query.getInt(query.getColumnIndex(DBConstant.localYear));
            Double totalSpend=query.getDouble(query.getColumnIndex(DBConstant.totalSpend));
            bean.setTotalSpend(StringUtils.keepDecimal(totalSpend));
            bean.setLocalYear(localYear);
            sbList.add(bean);
        }
        db.close();
        return sbList;
    }

    /**
     * 按月份分组
     * @return
     */
    public List<SpendBean> selectSpendGroupByMonth(int year){
        List<SpendBean> sbList=new ArrayList<SpendBean>();
        SpendBean bean;
        SQLiteDatabase db=getWritableDatabase();
        String sql="select a."+DBConstant.localYear+",a."+DBConstant.localMonth+",sum(a."+DBConstant.liveSpend+") as "+DBConstant.totalSpend+" from " + DBManager.T_Spend_Note + " as a " +
                " where "+DBConstant.localYear+"=? group by  "+DBConstant.localMonth+" order by  "+DBConstant.localMonth+" desc";
        Log.i("---","---"+sql);
        Cursor query = db.rawQuery(sql,new String[]{year+""});
        while (query.moveToNext()){
            bean=new SpendBean();
            int localYear=query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth=query.getInt(query.getColumnIndex(DBConstant.localMonth));
            Double totalSpend=query.getDouble(query.getColumnIndex(DBConstant.totalSpend));
            bean.setTotalSpend(StringUtils.keepDecimal(totalSpend));
            bean.setLocalYear(localYear);
            bean.setLocalMonth(localMonth);
            sbList.add(bean);
        }
        db.close();
        return sbList;
    }
    /**
     * 按天分组
     * @return
     */
    public List<SpendBean> selectSpendGroupByDay(int year, int month){
        List<SpendBean> sbList=new ArrayList<SpendBean>();
        SpendBean bean;
        SQLiteDatabase db=getWritableDatabase();
        String sql="select a."+DBConstant.localYear+",a."+DBConstant.localMonth+",a."+DBConstant.localDay+",sum(a."+DBConstant.liveSpend+") as "+DBConstant.totalSpend+" from " + DBManager.T_Spend_Note + " as a " +
                " where "+DBConstant.localYear+"=? and "+DBConstant.localMonth+"=? group by  "+DBConstant.localDay+" order by  "+DBConstant.localDay+" desc";
        Log.i("---","---"+sql);
        Cursor query = db.rawQuery(sql,new String[]{year+"",month+""});
        while (query.moveToNext()){
            bean=new SpendBean();
            int localYear=query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth=query.getInt(query.getColumnIndex(DBConstant.localMonth));
            int localDay=query.getInt(query.getColumnIndex(DBConstant.localDay));
            Double totalSpend=query.getDouble(query.getColumnIndex(DBConstant.totalSpend));
            bean.setTotalSpend(StringUtils.keepDecimal(totalSpend));
            bean.setLocalYear(localYear);
            bean.setLocalMonth(localMonth);
            bean.setLocalDay(localDay);
            sbList.add(bean);
        }
        db.close();
        return sbList;
    }

    /**
     * 获取一天的消费记录
     * @param year
     * @param month
     * @param day
     * @return
     */
    public List<SpendBean> selectSpendByOneDay(int year,int month,int day){
        List<SpendBean> sbList=new ArrayList<SpendBean>();
        SpendBean bean;
        SQLiteDatabase db=getWritableDatabase();
        String sql="select * from " + DBManager.T_Spend_Note +
                " where "+DBConstant.localYear+"=? and "+DBConstant.localMonth+"=? and "+DBConstant.localDay+"=? order by  "+DBConstant.creatTime+" desc";
        Log.i("---","---"+sql);
        Cursor query = db.rawQuery(sql,new String[]{year+"",month+"",day+""});
        while (query.moveToNext()){
            bean=new SpendBean();
            int id=query.getInt(query.getColumnIndex(DBConstant._id));
            Double liveSpend=query.getDouble(query.getColumnIndex(DBConstant.liveSpend));
            String liveSpendStr=query.getString(query.getColumnIndex(DBConstant.liveSpend));
            Log.i("=======",liveSpendStr+"=======liveSpend"+liveSpend);
            String dataRemark=query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime=query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime=query.getString(query.getColumnIndex(DBConstant.creatTime));
            int localYear=query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth=query.getInt(query.getColumnIndex(DBConstant.localMonth));
            int localDay=query.getInt(query.getColumnIndex(DBConstant.localDay));
            bean.set_id(id);
            bean.setLiveSpend(liveSpend);
            bean.setDataRemark(AES.decode(dataRemark));
            bean.setUpdateTime(DateUtils.stringToDate(updateTime, DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime, DateUtils.ymdhm));
            bean.setLocalYear(localYear);
            bean.setLocalMonth(localMonth );
            bean.setLocalDay(localDay );
            sbList.add(bean);
        }
        db.close();
        return sbList;
    }
    public SpendBean selectSpendForTree3(){
        SparseArray<SparseArray<SparseArray<List<SpendBean>>>>sparseArray=new SparseArray<SparseArray<SparseArray<List<SpendBean>>>>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor query = db.query(T_Spend_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataRemark,
                        DBConstant.liveSpend,
                        DBConstant.updateTime,
                        DBConstant.creatTime,
                        DBConstant.localYear,
                        DBConstant.localMonth,
                        DBConstant.localDay,
                }, null, null, null, null, "localYear desc,localMonth desc,localDay desc");
        List<SpendBean>list=new ArrayList<SpendBean>();
        SpendBean bean;

        SpendBean spendBean=new SpendBean();
        List<SpendBean>yList=new ArrayList<SpendBean>();
        SpendBean mSpendBean=new SpendBean();
        List<SpendBean>mList=new ArrayList<SpendBean>();
        SpendBean dSpendBean=new SpendBean();
        List<SpendBean>dList=new ArrayList<SpendBean>();
        SpendBean hSpendBean=new SpendBean();
        List<SpendBean>hList=new ArrayList<SpendBean>();
        while (query.moveToNext()){
            bean=new SpendBean();
            int id=query.getInt(query.getColumnIndex(DBConstant._id));
            Double liveSpend=query.getDouble(query.getColumnIndex(DBConstant.liveSpend));
            String dataRemark=query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime=query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime=query.getString(query.getColumnIndex(DBConstant.creatTime));
            int localYear=query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth=query.getInt(query.getColumnIndex(DBConstant.localMonth));
            int localDay=query.getInt(query.getColumnIndex(DBConstant.localDay));
            bean.set_id(id);
            bean.setLiveSpend(liveSpend);
            bean.setDataRemark(AES.decode(dataRemark));
            bean.setUpdateTime(DateUtils.stringToDate(updateTime, DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime, DateUtils.ymdhm));
            bean.setLocalYear(localYear );
            bean.setLocalMonth(localMonth );
            bean.setLocalDay(localDay );
            list.add(bean);
            SparseArray<SparseArray<List<SpendBean>>> mSparseArray;
            SparseArray<List<SpendBean>> dSparseArray;
            List<SpendBean> dayList;
            if(sparseArray.get(localYear)==null){
                Log.i("=========","==="+bean.getLocalYear()+"-"+bean.getLocalMonth()+"-"+bean.getLocalDay());
                dSparseArray=new SparseArray<List<SpendBean>>();
                mSparseArray=new SparseArray<SparseArray<List<SpendBean>>>();
                dayList=new ArrayList<SpendBean>();
                dayList.add(bean);
                dSparseArray.put(localDay, dayList);
                mSparseArray.put(localMonth, dSparseArray);
                sparseArray.put(localYear, mSparseArray);
                dList.add(bean);
                /////////////////////////////////
                hList=new ArrayList<>();
                hList.add(bean);
                hSpendBean=new SpendBean();
                hSpendBean.setLocalDay(localDay);
                hSpendBean.setList(hList);

                dList=new ArrayList<>();
                dList.add(hSpendBean);
                dSpendBean=new SpendBean();
                dSpendBean.setLocalMonth(localMonth);
                dSpendBean.setList(dList);

                mList=new ArrayList<>();
                mList.add(dSpendBean);
                mSpendBean=new SpendBean();
                mSpendBean.setLocalYear(localYear);
                mSpendBean.setList(mList);

                yList.add(mSpendBean);
//                spendBean=new SpendBean();
                spendBean.setList(yList);
                Log.i("------", "===" + bean.getLocalYear() + "-" + bean.getLocalMonth() + "-" + bean.getLocalDay());
            }else{
                if(sparseArray.get(localYear).get(localMonth)==null){
                    Log.i("=========","==="+bean.getLocalYear()+"-"+bean.getLocalMonth()+"-"+bean.getLocalDay());
                    dSparseArray=new SparseArray<List<SpendBean>>();
                    dayList=new ArrayList<SpendBean>();
                    dayList.add(bean);
                    dSparseArray.put(localDay, dayList);
                    sparseArray.get(localYear).put(localMonth,dSparseArray);
                    /////////////////////////////////
                    dList=new ArrayList<>();
                    dList.add(bean);
                    dSpendBean=new SpendBean();
                    dSpendBean.setList(dList);
                    dSpendBean.setLocalMonth(localMonth);

                    mList=new ArrayList<>();
                    mList.add(dSpendBean);
                    mSpendBean=new SpendBean();
                    mSpendBean.setList(mList);
                    mSpendBean.setLocalYear(localYear);

                    for (int i = 0; i < spendBean.getList().size(); i++) {
                        if(spendBean.getList().get(i).getLocalYear()==localYear){
                            spendBean.getList().get(i).getList().add(mSpendBean);
                            break;
                        }
                    }

                }else{
                    if(sparseArray.get(localYear).get(localMonth).get(localDay)==null){
                        Log.i("=========","==="+bean.getLocalYear()+"-"+bean.getLocalMonth()+"-"+bean.getLocalDay());
                        dayList=new ArrayList<SpendBean>();
                        dayList.add(bean);
                        sparseArray.get(localYear).get(localMonth).put(localDay,dayList);
                        /////////////////////////////////
                        dList=new ArrayList<>();
                        dList.add(bean);
                        dSpendBean=new SpendBean();
                        dSpendBean.setList(dList);
                        dSpendBean.setLocalMonth(localMonth);
                        for (int i = 0; i < spendBean.getList().size(); i++) {
                            if(spendBean.getList().get(i).getLocalYear()==localYear){
                                for (int j = 0; j < spendBean.getList().get(i).getList().size(); j++) {
                                    if(spendBean.getList().get(i).getList().get(j).getLocalMonth()==localMonth){
                                        spendBean.getList().get(i).getList().get(j).getList().add(dSpendBean);
                                        break;
                                    }
                                }
                            }
                        }
                    }else{
                        Log.i("=========","==="+bean.getLocalYear()+"-"+bean.getLocalMonth()+"-"+bean.getLocalDay());
                        sparseArray.get(localYear).get(localMonth).get(localDay).add(bean);
                        /////////////////////////////////
                        for (int i = 0; i < spendBean.getList().size(); i++) {
                            if(spendBean.getList().get(i).getLocalYear()==localYear){
                                for (int j = 0; j < spendBean.getList().get(i).getList().size(); j++) {
                                    if(spendBean.getList().get(i).getList().get(j).getLocalMonth()==localMonth){
                                        for (int k = 0; k < spendBean.getList().get(i).getList().get(j).getList().size(); k++) {
                                            if(spendBean.getList().get(i).getList().get(j).getList().get(k).getLocalDay()==localDay){
                                                spendBean.getList().get(i).getList().get(j).getList().get(k).getList().add(bean);
                                                break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
        db.close();
        Log.i("-----","---"+spendBean.getList().size());
        return spendBean;
    }

    public SparseArray<SparseArray<SparseArray<List<SpendBean>>>> selectSpendForTree(){
        SparseArray<SparseArray<SparseArray<List<SpendBean>>>>sparseArray=new SparseArray<SparseArray<SparseArray<List<SpendBean>>>>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor query = db.query(T_Spend_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataRemark,
                        DBConstant.liveSpend,
                        DBConstant.updateTime,
                        DBConstant.creatTime,
                        DBConstant.localYear,
                        DBConstant.localMonth,
                        DBConstant.localDay,
                }, null, null, null, null,"localYear desc,localMonth desc,localDay desc");
        List<SpendBean>list=new ArrayList<SpendBean>();
        SpendBean bean;

        SpendBean ySpendBean=new SpendBean();
        List<SpendBean>yList=new ArrayList<SpendBean>();
        SpendBean mSpendBean=new SpendBean();
        List<SpendBean>mList=new ArrayList<SpendBean>();
        SpendBean dSpendBean=new SpendBean();
        List<SpendBean>dList=new ArrayList<SpendBean>();
        while (query.moveToNext()){
            bean=new SpendBean();
            int id=query.getInt(query.getColumnIndex(DBConstant._id));
            Double liveSpend=query.getDouble(query.getColumnIndex(DBConstant.liveSpend));
            String dataRemark=query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime=query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime=query.getString(query.getColumnIndex(DBConstant.creatTime));
            int localYear=query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth=query.getInt(query.getColumnIndex(DBConstant.localMonth));
            int localDay=query.getInt(query.getColumnIndex(DBConstant.localDay));
            bean.set_id(id);
            bean.setLiveSpend(liveSpend);
            bean.setDataRemark(AES.decode(dataRemark));
            bean.setUpdateTime(DateUtils.stringToDate(updateTime, DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime, DateUtils.ymdhm));
            bean.setLocalYear(localYear );
            bean.setLocalMonth(localMonth );
            bean.setLocalDay(localDay );
            list.add(bean);
            SparseArray<SparseArray<List<SpendBean>>> mSparseArray;
            SparseArray<List<SpendBean>> dSparseArray;
            List<SpendBean> dayList;
            if(sparseArray.get(localYear)==null){
                dSparseArray=new SparseArray<List<SpendBean>>();
                mSparseArray=new SparseArray<SparseArray<List<SpendBean>>>();
                dayList=new ArrayList<SpendBean>();
                dayList.add(bean);
                dSparseArray.put(localDay, dayList);
                mSparseArray.put(localMonth,dSparseArray);
                sparseArray.put(localYear,mSparseArray);
                dList.add(bean);
            }else{
                if(sparseArray.get(localYear).get(localMonth)==null){
                    dSparseArray=new SparseArray<List<SpendBean>>();
                    dayList=new ArrayList<SpendBean>();
                    dayList.add(bean);
                    dSparseArray.put(localDay, dayList);
                    sparseArray.get(localYear).put(localMonth,dSparseArray);
                }else{
                    if(sparseArray.get(localYear).get(localMonth).get(localDay)==null){
                        dayList=new ArrayList<SpendBean>();
                        dayList.add(bean);
                        sparseArray.get(localYear).get(localMonth).put(localDay,dayList);
                    }else{
                        sparseArray.get(localYear).get(localMonth).get(localDay).add(bean);
                    }
                }
            }
        }
        db.close();
        return sparseArray;
    }
    public SparseArray<SparseArray<SparseArray<List<SpendBean>>>> selectSpendForTree2(){
        SparseArray<SparseArray<SparseArray<List<SpendBean>>>>sparseArray=new SparseArray<SparseArray<SparseArray<List<SpendBean>>>>();
        SQLiteDatabase db=getWritableDatabase();
        Cursor query = db.query(T_Spend_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataRemark,
                        DBConstant.liveSpend,
                        DBConstant.updateTime,
                        DBConstant.creatTime,
                        DBConstant.localYear,
                        DBConstant.localMonth,
                        DBConstant.localDay,
                }, null, null, null, null,"localYear desc,localMonth desc,localDay desc");
        List<SpendBean>list=new ArrayList<SpendBean>();
        SpendBean bean;

        SpendBean ySpendBean=new SpendBean();
        List<SpendBean>yList=new ArrayList<SpendBean>();
        SpendBean mSpendBean=new SpendBean();
        List<SpendBean>mList=new ArrayList<SpendBean>();
        SpendBean dSpendBean=new SpendBean();
        List<SpendBean>dList=new ArrayList<SpendBean>();
        while (query.moveToNext()){
            bean=new SpendBean();
            int id=query.getInt(query.getColumnIndex(DBConstant._id));
            Double liveSpend=query.getDouble(query.getColumnIndex(DBConstant.liveSpend));
            String dataRemark=query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime=query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime=query.getString(query.getColumnIndex(DBConstant.creatTime));
            int localYear=query.getInt(query.getColumnIndex(DBConstant.localYear));
            int localMonth=query.getInt(query.getColumnIndex(DBConstant.localMonth));
            int localDay=query.getInt(query.getColumnIndex(DBConstant.localDay));
            bean.set_id(id);
            bean.setLiveSpend(liveSpend);
            bean.setDataRemark(AES.decode(dataRemark));
            bean.setUpdateTime(DateUtils.stringToDate(updateTime, DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime, DateUtils.ymdhm));
            bean.setLocalYear(localYear );
            bean.setLocalMonth(localMonth );
            bean.setLocalDay(localDay );
            list.add(bean);
            SparseArray<SparseArray<List<SpendBean>>> mSparseArray;
            SparseArray<List<SpendBean>> dSparseArray;
            List<SpendBean> dayList;
            if(sparseArray.get(localYear)==null){
                dSparseArray=new SparseArray<List<SpendBean>>();
                mSparseArray=new SparseArray<SparseArray<List<SpendBean>>>();
                dayList=new ArrayList<SpendBean>();
                dayList.add(bean);
                dSparseArray.put(localDay, dayList);
                mSparseArray.put(localMonth,dSparseArray);
                sparseArray.put(localYear,mSparseArray);
                dList.add(bean);
            }else{
                if(sparseArray.get(localYear).get(localMonth)==null){
                    dSparseArray=new SparseArray<List<SpendBean>>();
                    dayList=new ArrayList<SpendBean>();
                    dayList.add(bean);
                    dSparseArray.put(localDay, dayList);
                    sparseArray.get(localYear).put(localMonth,dSparseArray);
                }else{
                    if(sparseArray.get(localYear).get(localMonth).get(localDay)==null){
                        dayList=new ArrayList<SpendBean>();
                        dayList.add(bean);
                        sparseArray.get(localYear).get(localMonth).put(localDay,dayList);
                    }else{
                        sparseArray.get(localYear).get(localMonth).get(localDay).add(bean);
                    }
                }
            }
        }
        db.close();
        return sparseArray;
    }
}
