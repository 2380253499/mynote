package com.zr.note.ui.main.entity;

import com.zr.note.base.BaseEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/10/13.
 */
public class JokeBean  extends BaseEntity implements Serializable{
    private String dataContent;
    private String dataRemark;
    public String getDataContent() {
        return dataContent;
    }

    public String getDataRemark() {
        return dataRemark;
    }

    public void setDataRemark(String dataRemark) {
        this.dataRemark = dataRemark;
    }

    public void setDataContent(String dataContent) {
        this.dataContent = dataContent;
    }
}
