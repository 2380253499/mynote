package com.fast.note.ui.main.entity;

import com.fast.note.base.BaseEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class AccountBean extends BaseEntity implements Serializable{
    /*dataSource TEXT,
dataAccount TEXT,
dataPassword TEXT,
dataRemark TEXT,
updateTime TimeStamp
creatTime TimeStamp */
    private String dataSource;
    private String dataAccount;
    private String dataPassword;
    private String dataRemark;


    public String getDataSource() {
        return dataSource;
    }

    public void setDataSource(String dataSource) {
        this.dataSource = dataSource;
    }

    public String getDataAccount() {
        return dataAccount;
    }

    public void setDataAccount(String dataAccount) {
        this.dataAccount = dataAccount;
    }

    public String getDataPassword() {
        return dataPassword;
    }

    public void setDataPassword(String dataPassword) {
        this.dataPassword = dataPassword;
    }

    public String getDataRemark() {
        return dataRemark;
    }

    public void setDataRemark(String dataRemark) {
        this.dataRemark = dataRemark;
    }

}
