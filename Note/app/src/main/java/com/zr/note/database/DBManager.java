package com.zr.note.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zr.note.tools.AES;
import com.zr.note.tools.DateUtils;
import com.zr.note.tools.LogUtils;
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
    private static final String dbName="MyNote";
    private static final int version=2;
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


    @Override
    public void onCreate(SQLiteDatabase db) {
        addDataTable(db);
    }

    private void addDataTable(SQLiteDatabase db) {
        if (noExistTable(db,T_Account_Note)) {
            db.execSQL(DBConstant.T_Account_Note);
        }
        if (noExistTable(db,T_Memo_Note)) {
            db.execSQL(DBConstant.T_Memo_Note);
        }
        if (noExistTable(db,T_Joke_Note)) {
            db.execSQL(DBConstant.T_Joke_Note);
        }
        if (noExistTable(db,T_Spend_Note)) {
            db.execSQL(DBConstant.T_Spend_Note);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        addDataTable(db);
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
    public boolean updateAccount(AccountBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.updateTime,"datetime('now','localtime')");
        values.put(DBConstant.dataAccount,bean.getDataAccount());
        values.put(DBConstant.dataPassword,bean.getDataPassword());
        values.put(DBConstant.dataRemark,bean.getDataRemark());
        values.put(DBConstant.dataSource, bean.getDataSource());
        int update = db.update(T_Account_Note, values, DBConstant._id, new String[]{bean.get_id() + ""});
        return update>0?true:false;
    }
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
    public List<AccountBean> selectAccount(){
        return selectAccount(true);
    }
    public List<AccountBean> selectAccount(boolean isOrderByCreateTime){
        String orderBy=DBConstant.updateTime+" desc";
        if(isOrderByCreateTime){
            orderBy=DBConstant.creatTime+" desc";
        }
        SQLiteDatabase db=getWritableDatabase();
        Cursor query = db.query(T_Account_Note,
                new String[]{
                        DBConstant._id,
                        DBConstant.dataSource,
                        DBConstant.dataAccount,
                        DBConstant.dataPassword,
                        DBConstant.dataRemark,
                        DBConstant.updateTime,
                        DBConstant.creatTime}, null, null, null, null,orderBy);
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
            bean.setUpdateTime(DateUtils.stringToDate(updateTime,DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime,DateUtils.ymdhm));
            list.add(bean);
        }
        db.close();
        return list;
    }
    public long addAccount(AccountBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataSource, AES.encode(bean.getDataSource()));
        values.put(DBConstant.dataAccount,AES.encode(bean.getDataAccount()));
        values.put(DBConstant.dataPassword,AES.encode(bean.getDataPassword()));
        values.put(DBConstant.dataRemark, AES.encode(bean.getDataRemark()));
        long insert = db.insert(T_Account_Note, null, values);
        LogUtils.Log(insert);
        db.close();
        return insert;
    }
    public long addMemo(MemoBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataRemark,AES.encode(bean.getDataRemark()));
        values.put(DBConstant.dataContent, AES.encode(bean.getDataContent()));
        long insert = db.insert(T_Memo_Note, null, values);
        LogUtils.Log(insert);
        db.close();
        return insert;
    }
    public List<MemoBean> selectMemo(){
        return selectMemo(true);
    }
    public List<MemoBean> selectMemo(boolean isOrderByCreateTime){
        String orderBy=DBConstant.updateTime+" desc";
        if(isOrderByCreateTime){
            orderBy=DBConstant.creatTime+" desc";
        }
        SQLiteDatabase db=getWritableDatabase();
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
            bean.setUpdateTime(DateUtils.stringToDate(updateTime,DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime,DateUtils.ymdhm));
            list.add(bean);
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

    public long addJoke(JokeBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataRemark, AES.encode(bean.getDataRemark()));
        values.put(DBConstant.dataContent, AES.encode(bean.getDataContent()));
        long insert = db.insert(T_Joke_Note, null, values);
        LogUtils.Log(insert);
        db.close();
        return insert;
    }
    public List<JokeBean> selectJoke(){
        return selectJoke(true);
    }
    public List<JokeBean> selectJoke(boolean isOrderByCreateTime){
        String orderBy=DBConstant.updateTime+" desc";
        if(isOrderByCreateTime){
            orderBy=DBConstant.creatTime+" desc";
        }
        SQLiteDatabase db=getWritableDatabase();
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
            bean.setUpdateTime(DateUtils.stringToDate(updateTime,DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime,DateUtils.ymdhm));
            list.add(bean);
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
    public List<SpendBean> selectSpend(){
        return selectSpend(true);
    }
    public List<SpendBean> selectSpend(boolean isOrderByCreateTime){
        String orderBy=DBConstant.updateTime+" desc";
        if(isOrderByCreateTime){
            orderBy=DBConstant.creatTime+" desc";
        }
        SQLiteDatabase db=getWritableDatabase();
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
            String liveSpend=query.getString(query.getColumnIndex(DBConstant.liveSpend));
            String dataRemark=query.getString(query.getColumnIndex(DBConstant.dataRemark));
            String updateTime=query.getString(query.getColumnIndex(DBConstant.updateTime));
            String creatTime=query.getString(query.getColumnIndex(DBConstant.creatTime));
            bean.set_id(id);
            bean.setLiveSpend(Double.parseDouble(AES.decode(liveSpend)));
            bean.setDataRemark(AES.decode(dataRemark));
            bean.setUpdateTime(DateUtils.stringToDate(updateTime,DateUtils.ymdhm));
            bean.setCreatTime(DateUtils.stringToDate(creatTime,DateUtils.ymdhm));
            list.add(bean);
        }
        db.close();
        return list;
    }
    public long addSpend(SpendBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(DBConstant.dataRemark, AES.encode(bean.getDataRemark()));
        values.put(DBConstant.liveSpend, AES.encode(bean.getLiveSpend()+""));
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
    }
}
