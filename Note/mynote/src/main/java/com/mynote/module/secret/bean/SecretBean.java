package com.mynote.module.secret.bean;


import com.mynote.base.BaseEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class SecretBean extends BaseEntity implements Serializable{

    /*dataContent
    dataRemark
    updateTime
    createTime T*/
    private String dataRemark;
    private String dataContent;

    public String getDataRemark() {
        return dataRemark;
    }

    public void setDataRemark(String dataRemark) {
        this.dataRemark = dataRemark;
    }

    public String getDataContent() {
        return dataContent;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }
}
