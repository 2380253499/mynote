package com.zr.note.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.zr.note.tools.LogUtils;
import com.zr.note.ui.main.entity.AccountBean;

/**
 * Created by Administrator on 2016/10/11.
 */
public class DBManager extends SQLiteOpenHelper{
    private static final String dbName="MyNote";
    private static final int version=1;
    private static DBManager dbManager;
    public static final String T_Account_Note="T_Account_Note";
    public static final String T_Remark_Note="T_Remark_Note";
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
        if (noExistTable(db,T_Remark_Note)) {
            db.execSQL(DBConstant.T_Remark_Note);
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
    /**************************************保存数据方法************************************************/
    public void addAccount(AccountBean bean){
        SQLiteDatabase db=getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put("dataSource",bean.getDataSource());
        values.put("dataAccount",bean.getDataAccount());
        values.put("dataPassword",bean.getDataPassword());
        values.put("dataRemark", bean.getDataRemark());
        long insert = db.insert(T_Account_Note, null, values);
        LogUtils.Log(insert);
        db.close();
    }

}
