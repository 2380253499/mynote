package com.zr.note.database;

/**
 * Created by Administrator on 2016/10/11.
 */
public class DBConstant {
    //note_account_note
    public static final String addAccountNote="create table choosepeople (_id INTEGER PRIMARY KEY AUTOINCREMENT ,userId TEXT, realName TEXT, orgName TEXT, orgId TEXT, jobCode TEXT, jobName TEXT,updatetime TimeStamp NOT NULL DEFAULT (datetime('now','localtime')))";
}
