package com.zr.note.database;

/**
 * Created by Administrator on 2016/10/11.
 */
public class DBConstant {
    /**************************账户表字段************************************/
    public static final String _id="_id";
    public static final String dataSource="dataSource";
    public static final String dataAccount="dataAccount";
    public static final String dataPassword="dataPassword";
    public static final String dataRemark="dataRemark";
    public static final String updateTime="updateTime";
    public static final String creatTime="creatTime";
    /**************************备忘录表字段************************************/
    public static final String dataContent="dataContent";
    /**************************消费表字段************************************/
    public static final String liveSpend="liveSpend";
    /**
     * "dataSource,         --账号所属平台或者来源"
     * "dataAccount,        --账号"
     * "dataPassword,       --密码"
     * "dataRemark,         --备注"
     */
    public static final String T_Account_Note = "create table "+DBManager.T_Account_Note+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "dataSource TEXT," +
            "dataAccount TEXT," +
            "dataPassword TEXT," +
            "dataRemark TEXT," +
            "updateTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) ," +
            "creatTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )";
    /**
     * dataContent  --数据内容
     * dataRemark   --备注
     */
    public static final String T_Memo_Note = "create table "+DBManager.T_Memo_Note+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "dataRemark TEXT," +
            "dataContent TEXT," +
            "updateTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) ," +
            "creatTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )";
    /**
     * dataContent  --数据内容
     */
    public static final String T_Joke_Note = "create table "+DBManager.T_Joke_Note+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "dataRemark TEXT," +
            "dataContent TEXT," +
            "updateTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) ," +
            "creatTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )";
    /**
     * liveSpend DOUBLE,   --生活消费
     * dataRemark TEXT,         --备注
     */
    public static final String T_Spend_Note = "create table "+DBManager.T_Spend_Note+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "liveSpend DOUBLE," +
            "dataRemark TEXT," +
            "localYear  VARCHAR2 NOT NULL DEFAULT (strftime('%Y','now'))," +
            "localMonth VARCHAR2 NOT NULL DEFAULT (strftime('%m','now'))," +
            "localDay   VARCHAR2 NOT NULL DEFAULT (strftime('%d','now'))," +
            "updateTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) ," +
            "creatTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )";
}
