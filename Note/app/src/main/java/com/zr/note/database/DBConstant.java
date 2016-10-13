package com.zr.note.database;

/**
 * Created by Administrator on 2016/10/11.
 */
public class DBConstant {
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
            "updateTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )," +
            "creatTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )";
    /**
     * dataContent  --数据内容
     * dataRemark   --备注
     */
    public static final String T_Remark_Note = "create table "+DBManager.T_Remark_Note+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "dataContent TEXT," +
            "dataRemark TEXT," +
            "updateTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )," +
            "creatTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )";
    /**
     * dataContent  --数据内容
     */
    public static final String T_Joke_Note = "create table "+DBManager.T_Joke_Note+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "dataContent TEXT,   --数据内容" +
            "updateTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )," +
            "creatTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )";
    /**
     * liveSpend DOUBLE,   --生活消费
     * dataRemark TEXT,         --备注
     */
    public static final String T_Spend_Note = "create table "+DBManager.T_Spend_Note+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "liveSpend DOUBLE," +
            "dataRemark TEXT," +
            "updateTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )," +
            "creatTime TimeStamp NOT NULL DEFAULT (datetime('now','localtime') ) )";
}
