package com.mynote.module.home.bean;

import com.mynote.base.BaseEntity;

/**
 * Created by Administrator on 2018/2/26.
 */

public class TableBean extends BaseEntity{
    private String tableName;

    public TableBean(String tableName,boolean check) {
        this.tableName = tableName;
        setCheck(check);
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}
