package com.mynote.database;

/**
 * Created by Administrator on 2016/10/11.
 */
public class DBConstant {
    public static final String _id="_id";//自增长
    public static final String uid="uid";//多表情况的唯一标识
    public static final String dataRemark="dataRemark";//备注
    public static final String updateTime="updateTime";//修改时间 时间戳
    public static final String createTime ="creatTime";//创建时间 时间戳
    /**************************账户表字段************************************/
    public static final String dataSource="dataSource";//来源
    public static final String dataAccount="dataAccount";//账户
    public static final String dataPassword="dataPassword";//密码
    /**************************备忘录表字段************************************/
    public static final String dataContent="dataContent";//备忘内容
    /**************************消费表字段************************************/
    public static final String liveSpend="liveSpend";//消费
    public static final String localYear="localYear";//年
    public static final String localMonth="localMonth";//月
    public static final String localDay="localDay";//日
    public static final String totalSpend="totalSpend";//统计总消费时用到
    /**************************生日表字段************************************/
    public static final String peopleName="peopleName";
    public static final String peopleYear="peopleYear";
    public static final String peopleMonth="peopleMonth";
    public static final String peopleDay="peopleDay";
    public static final String distanceDay="distanceDay";
    public static final String tiXing="tiXing";
    /**
     * "dataSource,         --账号所属平台或者来源"
     * "dataAccount,        --账号"
     * "dataPassword,       --密码"
     * "dataRemark,         --备注"
     */
    public static final String CT_Account_Note = "create table "+DBManager.T_Account_Note+" (" +
            _id+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            uid+" varchar," +
            dataSource+" NTEXT," +
            dataAccount+"  NTEXT," +
            dataPassword+"  NTEXT," +
            dataRemark+"  NTEXT," +
            updateTime+"  NTEXT NOT NULL ," +
            createTime +"  NTEXT NOT NULL )";
    /**
     * dataContent  --数据内容
     * dataRemark   --备注
     */
    public static final String CT_Memo_Note = "create table "+DBManager.T_Memo_Note+" (" +
            "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
            uid+" varchar," +
            dataRemark+" NTEXT," +
            dataContent+" NTEXT," +
            updateTime+" NTEXT NOT NULL," +
            createTime +" NTEXT NOT NULL)";
    /**
     * dataContent  --数据内容
     */
    public static final String CT_Joke_Note = "create table "+DBManager.T_Joke_Note+" (" +
            _id+" INTEGER PRIMARY KEY AUTOINCREMENT," +
            uid+" varchar," +
            dataRemark+" NTEXT," +
            dataContent+" NTEXT," +
            updateTime+" NTEXT NOT NULL  ," +
            createTime +" NTEXT NOT NULL  )";
    /**
     * liveSpend DOUBLE,   --生活消费
     * dataRemark NTEXT,         --备注
     */
    public static final String CT_Spend_Note = "create table "+DBManager.T_Spend_Note+" (" +
            _id +" INTEGER PRIMARY KEY AUTOINCREMENT," +
            uid+" varchar, " +
            liveSpend  +" DOUBLE, " +
            dataRemark +" NTEXT, " +
            localYear  +" INTEGER NOT NULL DEFAULT (strftime('%Y','now'))," +
            localMonth +" INTEGER NOT NULL DEFAULT (strftime('%m','now'))," +
            localDay   +" INTEGER NOT NULL DEFAULT (strftime('%d','now'))," +
            updateTime +" NTEXT NOT NULL    ," +
            createTime +" NTEXT NOT NULL    )";
    /**
     * dataContent  --数据内容
     * dataRemark   --备注
     */
    public static final String CT_Spend_Dic_Note = "create table "+DBManager.T_Spend_Dic_Note+" (" +
            _id         +" INTEGER PRIMARY KEY AUTOINCREMENT," +
            uid+" varchar," +
            dataRemark  +"  NTEXT," +
            dataContent +" NTEXT," +
            updateTime  +" NTEXT NOT NULL   ," +
            createTime +" NTEXT NOT NULL  )";
    /**
     * dataContent  --数据内容
     * dataRemark   --备注
     */
    public static final String CT_Secret_Note = "create table "+DBManager.T_Secret_Note+" (" +
            _id         +" INTEGER PRIMARY KEY AUTOINCREMENT," +
            uid+" varchar," +
            dataRemark  +"  NTEXT," +
            dataContent +" NTEXT," +
            updateTime  +" NTEXT NOT NULL   ," +
            createTime +" NTEXT NOT NULL  )";


}
